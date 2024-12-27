package cl.angel.demo.repositorio.ayudante;

import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author angelexperti
 */
@Component
public class AyudanteDB {

    private final DataSource dataSource;

    @Autowired
    public AyudanteDB(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Empleado getEmpleado(Long id) throws SQLException {
        Empleado empleado = null;
        if (id != null) {
            try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement("SELECT * FROM empleados WHERE pk = ?")) {
                pst.setLong(1, id);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    Long rut = rs.getLong("rut");
                    String nombres = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");
                    LocalDate contratacion = rs.getDate("contratacion").toLocalDate();
                    LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                    LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                    empleado = new Empleado();
                    empleado.setId(id);
                    empleado.setRut(rut);
                    empleado.setNombres(nombres);
                    empleado.setApellidos(apellidos);
                    empleado.setContratacion(contratacion);
                    empleado.setCreado(creado);
                    empleado.setActualizado(actualizado);

                    rs.close();
                }
            }
        }
        return empleado;
    }

    public Aprobacion getAprobacion(Long id) throws SQLException {
        Aprobacion aprobacion = null;
        if (id != null) {
            Connection connection = getConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM aprobaciones WHERE pk = ?");
            pst.setLong(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Long empleadoId = rs.getLong("empleado_fk");
                String motivo = rs.getString("motivo");
                boolean aceptado = rs.getBoolean("aceptado");
                LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                Empleado empleado = getEmpleado(empleadoId);

                aprobacion = new Aprobacion();
                aprobacion.setId(id);
                aprobacion.setEmpleado(empleado);
                aprobacion.setMotivo(motivo);
                aprobacion.setAceptado(aceptado);
                aprobacion.setCreado(creado);
                aprobacion.setActualizado(actualizado);

                rs.close();
            }
            pst.close();

            connection.close();
        }
        return aprobacion;
    }

    public Aprobacion getAprobacion(Empleado empleado, String motivoSolicitud) throws SQLException {
        Aprobacion aprobacion = null;
        if (StringUtils.isNotBlank(motivoSolicitud) && empleado != null) {
            try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement("SELECT * FROM aprobaciones WHERE UPPER(motivo) = UPPER(?) AND empleado_fk = ?")) {
                pst.setString(1, motivoSolicitud);
                pst.setLong(2, empleado.getId());

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        Long id = rs.getLong("pk");
                        Long empleado_fk = rs.getLong("empleado_fk");
                        String motivo = rs.getString("motivo");
                        boolean aceptado = rs.getBoolean("aceptado");
                        LocalDateTime creado = rs.getTimestamp("creado").toLocalDateTime();
                        LocalDateTime actualizado = rs.getTimestamp("actualizado").toLocalDateTime();

                        Empleado empleadoEncontrado = getEmpleado(empleado_fk);

                        aprobacion = new Aprobacion();

                        aprobacion.setId(id);
                        aprobacion.setEmpleado(empleadoEncontrado);
                        aprobacion.setMotivo(motivo);
                        aprobacion.setAceptado(aceptado);
                        aprobacion.setCreado(creado);
                        aprobacion.setActualizado(actualizado);
                    }
                }
            }
        }
        return aprobacion;
    }
}
