package cl.angel.demo.repositorio;

import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.modelo.Vacacion;
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
import org.springframework.stereotype.Component;

/**
 *
 * @author angelexperti
 */
@Component
public class RepoVacacion {

    private final AyudanteDB ayudante;

    @Autowired
    public RepoVacacion(AyudanteDB ayudante) {
        this.ayudante = ayudante;
    }

    public void creacion(Vacacion vacacion) throws SQLException {
        if (vacacion != null && vacacion.getAprobacion() != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("INSERT INTO vacaciones (aprobacion_fk, fecha_libre, creado, actualizado) VALUES (?, ?, ?, ?)")) {

                pst.setLong(1, vacacion.getAprobacion().getId());
                pst.setDate(2, java.sql.Date.valueOf(vacacion.getFechaLibre()));
                pst.setTimestamp(3, java.sql.Timestamp.valueOf(vacacion.getCreado()));
                pst.setTimestamp(4, java.sql.Timestamp.valueOf(vacacion.getActualizado()));
                pst.execute();
            }
        }
    }

    public List<Vacacion> lectura() throws SQLException {
        List<Vacacion> vacaciones = new ArrayList<>();
        try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("SELECT * FROM vacaciones"); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("pk");
                Long aprobacion_fk = rs.getLong("aprobacion_fk");
                LocalDate fecha_libre = rs.getDate("fecha_libre").toLocalDate();
                LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                Aprobacion aprobacion = ayudante.getAprobacion(aprobacion_fk);

                Vacacion vacacion = new Vacacion();

                vacacion.setId(id);

                vacacion.setAprobacion(aprobacion);
                vacacion.setFechaLibre(fecha_libre);
                vacacion.setCreado(creado);
                vacacion.setActualizado(actualizado);

                vacaciones.add(vacacion);
            }
        }

        return vacaciones;
    }

    public Vacacion lectura(Long id) throws SQLException {
        Vacacion vacacion = null;
        if (id != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("SELECT * FROM vacaciones WHERE pk = ?")) {
                pst.setLong(1, id);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        Long aprobacion_fk = rs.getLong("aprobacion_fk");
                        LocalDate fecha_libre = rs.getDate("fecha_libre").toLocalDate();
                        LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                        LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                        Aprobacion aprobacion = ayudante.getAprobacion(aprobacion_fk);

                        vacacion = new Vacacion();

                        vacacion.setId(id);
                        vacacion.setAprobacion(aprobacion);
                        vacacion.setFechaLibre(fecha_libre);
                        vacacion.setCreado(creado);
                        vacacion.setActualizado(actualizado);
                    }
                }
            }
        }
        return vacacion;
    }

    public void actualizacion(Vacacion vacacion) throws SQLException {
        if (vacacion != null && vacacion.getAprobacion() != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("UPDATE vacaciones SET aprobacion_fk=?, fecha_libre=?, actualizado=NOW() WHERE pk=?")) {

                pst.setLong(1, vacacion.getAprobacion().getId());
                pst.setDate(4, java.sql.Date.valueOf(vacacion.getFechaLibre()));
                pst.setLong(4, vacacion.getId());

                pst.execute();
            }
        }
    }

    public void eliminar(Vacacion vacacion) throws SQLException {
        if (vacacion != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("DELETE FROM vacaciones WHERE pk=?")) {

                pst.setLong(1, vacacion.getId());

                pst.execute();
            }
        }
    }

    public List<Vacacion> obtenerVacaciones(Empleado empleado) throws SQLException {
        List<Vacacion> vacaciones = new ArrayList<>();
        if (empleado != null) {
            try (Connection connection = ayudante.getConnection(); PreparedStatement pst = connection.prepareStatement("select v.* from vacaciones v, aprobaciones a where v.aprobacion_fk =a.pk  and a.empleado_fk = ?")) {
                pst.setLong(1, empleado.getId());
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("pk");
                        Long aprobacion_fk = rs.getLong("aprobacion_fk");
                        LocalDate fecha_libre = rs.getDate("fecha_libre").toLocalDate();
                        LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                        LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                        Aprobacion aprobacion = ayudante.getAprobacion(aprobacion_fk);

                        Vacacion vacacion = new Vacacion();
                        vacacion.setId(id);
                        vacacion.setAprobacion(aprobacion);
                        vacacion.setFechaLibre(fecha_libre);
                        vacacion.setCreado(creado);
                        vacacion.setActualizado(actualizado);
                        vacaciones.add(vacacion);
                    }
                }
            }
        }
        return vacaciones;
    }
}
