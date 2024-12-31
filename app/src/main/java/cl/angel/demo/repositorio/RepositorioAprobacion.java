package cl.angel.demo.repositorio;
import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author angelexperti
 */
@Repository
public interface RepositorioAprobacion extends JpaRepository<Aprobacion, Long> {

    public Aprobacion findByMotivoIgnoreCaseAndEmpleado(String motivo, Empleado empleado);

    public List<Aprobacion> findByEmpleado(Empleado empleado);
}
