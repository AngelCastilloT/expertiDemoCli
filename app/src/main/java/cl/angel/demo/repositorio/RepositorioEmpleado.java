package cl.angel.demo.repositorio;

import cl.angel.demo.modelo.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author angelexperti
 */
@Repository
public interface RepositorioEmpleado extends JpaRepository<Empleado, Long> {

    public Empleado findByRut(Long rut);
}
