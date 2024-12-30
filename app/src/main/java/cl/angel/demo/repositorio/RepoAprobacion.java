package cl.angel.demo.repositorio;

import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.repositorio.ayudante.AyudanteDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author angel
 */
@Component
public class RepoAprobacion {

    private final AyudanteDB ayudante;

    @Autowired
    public RepoAprobacion(AyudanteDB ayudante) {
        this.ayudante = ayudante;
    }

    public void creacion(Aprobacion aprobacion) throws SQLException {
        if (aprobacion != null && aprobacion.getEmpleado() != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("INSERT INTO aprobaciones (empleado_fk, motivo, aceptado, creado, actualizado) VALUES (?, ?, ?, ?, ?)")) {

                pst.setLong(1, aprobacion.getEmpleado().getId());

                pst.setString(2, aprobacion.getMotivo());
                pst.setBoolean(3, aprobacion.isAceptado());
                pst.setTimestamp(4, java.sql.Timestamp.valueOf(aprobacion.getCreado()));
                pst.setTimestamp(5, java.sql.Timestamp.valueOf(aprobacion.getActualizado()));
                pst.execute();
            }
        } 
    }

    public List<Aprobacion> lectura() throws SQLException {
        List<Aprobacion> aprobaciones = new ArrayList<>();
        try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("SELECT * FROM aprobaciones"); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("pk");
                Long empleado_fk = rs.getLong("empleado_fk");
                String motivo = rs.getString("motivo");
                boolean aceptado = rs.getBoolean("aceptado");
                LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                Empleado empleado = ayudante.getEmpleado(empleado_fk);

                Aprobacion aprobacion = new Aprobacion();

                aprobacion.setId(id);

                aprobacion.setEmpleado(empleado);
                aprobacion.setMotivo(motivo);
                aprobacion.setAceptado(aceptado);
                aprobacion.setCreado(creado);
                aprobacion.setActualizado(actualizado);

                aprobaciones.add(aprobacion);
            }
        }

        return aprobaciones;
    }

    public Aprobacion lectura(Empleado empleado, String motivoSolicitud) throws SQLException {
        return ayudante.getAprobacion(empleado, motivoSolicitud);
    }
    
    public void actualizacion(Aprobacion aprobacion) throws SQLException {
        if (aprobacion != null && aprobacion.getEmpleado() != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("UPDATE aprobaciones SET empleado_fk=?, motivo=?, aceptado=?, actualizado=NOW() WHERE pk=?")) {

                pst.setLong(1, aprobacion.getEmpleado().getId());

                pst.setString(2, aprobacion.getMotivo());
                pst.setBoolean(3, aprobacion.isAceptado());
                pst.setLong(4, aprobacion.getId());

                pst.execute();
            }
        }
    }

    public void eliminar(Aprobacion aprobacion) throws SQLException {
        if (aprobacion != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("DELETE FROM aprobaciones WHERE pk=?")) {

                pst.setLong(1, aprobacion.getId());

                pst.execute();
            }
        }
    }

}
