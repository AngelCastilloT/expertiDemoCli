package cl.angel.demo.repositorio;
import cl.angel.demo.modelo.Empleado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author angelexperti
 */
@Repository
public interface RepositorioEmpleado extends JpaRepository<Empleado, Long> {

    public Empleado findByRut(Long rut);

    public List<Empleado> findBy();
}
