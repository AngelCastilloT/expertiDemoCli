package cl.angel.demo.repositorio;

import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.modelo.Vacacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author angelexperti
 */
@Repository
public interface RepositorioVacacion extends JpaRepository<Vacacion, Long> {

    public List<Vacacion> findByAprobacion(Aprobacion aprobacion);

    public List<Vacacion> findByAprobacionEmpleadoAndAprobacionMotivoIgnoreCase(Empleado empleado, String motivo);

    public List<Vacacion> findByAprobacionEmpleado(Empleado empleado);

}
