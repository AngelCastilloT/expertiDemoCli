package cl.angel.demo.administrador;
import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.repositorio.RepositorioEmpleado;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 * @author angelexperti
 */
@Service
public class AdministradorEmpleado {

    private final RepositorioEmpleado repositorioEmpleado;

    @Autowired
    public AdministradorEmpleado(RepositorioEmpleado repositorioEmpleado) {
        this.repositorioEmpleado = repositorioEmpleado;
    }
    
    public Empleado consultar(Long rut) {
        Empleado emp = null;
        if (rut != null) {
            emp = repositorioEmpleado.findByRut(rut);
        }
        return emp;
    }
    
    public List<Empleado> consultarTodos(){
        List<Empleado> emp = new ArrayList<>();
        emp = repositorioEmpleado.findBy();
        return emp;
    }
    
    public Empleado guardar(Empleado emp) {
        Empleado guardado = null;
        if (emp != null) {
            guardado = repositorioEmpleado.saveAndFlush(emp);
        }
        return guardado;
    }

    public void eliminar(Empleado emp) {
        if (emp != null) {
            repositorioEmpleado.delete(emp);
        }
    }
}
