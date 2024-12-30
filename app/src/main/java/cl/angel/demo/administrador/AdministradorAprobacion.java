package cl.angel.demo.administrador;

import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.repositorio.RepositorioAprobacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.angel.demo.modelo.Empleado;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author angelexperti
 */
@Service
public class AdministradorAprobacion {

    private final RepositorioAprobacion repositorioAprobacion;

    @Autowired
    public AdministradorAprobacion(RepositorioAprobacion repositorioAprobacion) {
        this.repositorioAprobacion = repositorioAprobacion;
    }

    public Aprobacion consultar(String motivo, Empleado empleado) {
        Aprobacion apr = null;
        if (empleado != null && StringUtils.isNotBlank(motivo)) {
            apr = repositorioAprobacion.findByMotivoIgnoreCaseAndEmpleado(motivo, empleado);
        }
        return apr;
    }

    public List<Aprobacion> consultar(Empleado empleado) {
        List<Aprobacion> lista = new ArrayList<>();
        if (empleado != null) {
            lista = repositorioAprobacion.findByEmpleado(empleado);
        }
        return lista;
    }

    public Aprobacion guardar(Aprobacion apr) {
        Aprobacion guardado = null;
        if (apr != null) {
            guardado = repositorioAprobacion.saveAndFlush(apr);
        }
        return guardado;
    }

    public void eliminar(Aprobacion apr) {
        if (apr != null) {
            repositorioAprobacion.delete(apr);
        }
    }

}
