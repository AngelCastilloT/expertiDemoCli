package cl.angel.demo.repositorio;

import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.repositorio.ayudante.AyudanteDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author angelexperti
 */
@Service
public class RepoEmpleado {

    private final AyudanteDB ayudante;

    @Autowired
    public RepoEmpleado(AyudanteDB ayudante) {
        this.ayudante = ayudante;
    }

    public void creacion(Empleado empleado) throws SQLException {
        if (empleado != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("INSERT INTO empleados (rut, nombres, apellidos, contratacion, creado, actualizado) VALUES (?, ?, ?, ?, ?, ?)")) {
                pst.setLong(1, empleado.getRut());
                pst.setString(2, empleado.getNombres());
                pst.setString(3, empleado.getApellidos());
                pst.setDate(4, java.sql.Date.valueOf(empleado.getContratacion()));
                pst.setTimestamp(5, java.sql.Timestamp.valueOf(empleado.getCreado()));
                pst.setTimestamp(6, java.sql.Timestamp.valueOf(empleado.getActualizado()));
                pst.execute();
            }
        }
    }

    public List<Empleado> lectura() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("SELECT * FROM empleados")) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("pk");
                Long rut = rs.getLong("rut");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                LocalDate contratacion = rs.getDate("contratacion").toLocalDate();
                LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                Empleado empleado = new Empleado();
                empleado.setActualizado(actualizado);
                empleado.setApellidos(apellidos);
                empleado.setCreado(creado);
                empleado.setContratacion(contratacion);
                empleado.setId(id);
                empleado.setNombres(nombres);
                empleado.setRut(rut);
                empleados.add(empleado);
            }
            rs.close();
        }

        return empleados;
    }

    public Empleado lectura(Long rut) throws SQLException {
        Empleado empleado = null;
        if (rut != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("SELECT * FROM empleados WHERE rut = ?")) {
                pst.setLong(1, rut);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    Long id = rs.getLong("pk");
                    String nombres = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");
                    LocalDate contratacion = rs.getDate("contratacion").toLocalDate();
                    LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                    LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                    empleado = new Empleado();
                    empleado.setActualizado(actualizado);
                    empleado.setApellidos(apellidos);
                    empleado.setCreado(creado);
                    empleado.setContratacion(contratacion);
                    empleado.setId(id);
                    empleado.setNombres(nombres);
                    empleado.setRut(rut);

                    rs.close();
                }
            }
        }
        return empleado;
    }

    public void actualizacion(Empleado empleado) throws SQLException {
        if (empleado != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("UPDATE empleados SET rut=?, nombres=?2, apellidos=?, contratacion=?, actualizado=NOW() WHERE pk=?")) {
                pst.setLong(1, empleado.getRut());
                pst.setString(2, empleado.getNombres());
                pst.setString(3, empleado.getApellidos());
                pst.setDate(4, java.sql.Date.valueOf(empleado.getContratacion()));
                pst.setLong(5, empleado.getId());
                pst.execute();
            }
        }
    }

    public void eliminar(Empleado empleado) throws SQLException {
        if (empleado != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("DELETE FROM empleados WHERE pk=?")) {
                pst.setLong(1, empleado.getId());
                pst.execute();
            }
        }
    }
}
