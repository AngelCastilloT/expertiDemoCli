package cl.angel.demo.administrador;
import cl.angel.demo.excepcion.AngelException;
import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.modelo.Vacacion;
import cl.angel.demo.repositorio.RepositorioAprobacion;
import cl.angel.demo.repositorio.RepositorioVacacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author angelexperti
 */
@Service
public class AdministradorVacacion {

    private final RepositorioAprobacion repositorioAprobacion;
    private final RepositorioVacacion repositorioVacacion;

    @Autowired
    public AdministradorVacacion(RepositorioAprobacion repositorioAprobacion, RepositorioVacacion repositorioVacacion) {
        this.repositorioAprobacion = repositorioAprobacion;
        this.repositorioVacacion = repositorioVacacion;
    }

    public List<Vacacion> consultar(String motivo, Empleado empleado) {
        List<Vacacion> lista = new ArrayList<>();
        if (StringUtils.isNotBlank(motivo) && empleado != null) {
            Aprobacion aprobacion = repositorioAprobacion.findByMotivoIgnoreCaseAndEmpleado(motivo, empleado);
            if (aprobacion != null) {
                lista = repositorioVacacion.findByAprobacion(aprobacion);
            }
        }
        return lista;
    }

    public List<Vacacion> consultar(Empleado empleado) {
        List<Vacacion> lista = new ArrayList<>();
        if (empleado != null) {
            lista = repositorioVacacion.findByAprobacionEmpleado(empleado);
        }
        return lista;
    }

    public Vacacion guardar(Vacacion vac) {
        Vacacion guardado = null;
        if (vac != null) {
            guardado = repositorioVacacion.saveAndFlush(vac);
        }
        return guardado;
    }

    public void eliminar(Vacacion vac) {
        if (vac != null) {
            repositorioVacacion.delete(vac);
        }
    }

    @Transactional
    public boolean solicitar(Empleado empleado, String motivo, List<LocalDate> dias) {
        boolean ok = false;
        if (empleado != null && StringUtils.isNotBlank(motivo) && CollectionUtils.isNotEmpty(dias)) {
            Aprobacion aprobacion = repositorioAprobacion.findByMotivoIgnoreCaseAndEmpleado(motivo, empleado);
            if (aprobacion == null) {
                Aprobacion nuevaAprobacion = new Aprobacion();
                nuevaAprobacion.setMotivo(motivo);
                nuevaAprobacion.setEmpleado(empleado);
                nuevaAprobacion.setAceptado(false);
                aprobacion = repositorioAprobacion.saveAndFlush(nuevaAprobacion);
            }

            List<Vacacion> vacaciones = new ArrayList<>();
            for (LocalDate dia : dias) {
                Vacacion v = new Vacacion();
                v.setAprobacion(aprobacion);
                v.setFechaLibre(dia);
                vacaciones.add(v);
            }

            List<Vacacion> lista = repositorioVacacion.saveAllAndFlush(vacaciones);
            ok = CollectionUtils.isNotEmpty(lista);
        }
        return ok;
    }

    @Transactional
    public boolean aceptar(Empleado empleado, String motivo) {
        boolean ok = false;
        if (empleado != null && StringUtils.isNotBlank(motivo)) {
            Aprobacion aprobacion = repositorioAprobacion.findByMotivoIgnoreCaseAndEmpleado(motivo, empleado);
            if (aprobacion != null) {
                aprobacion.setAceptado(true);
                aprobacion.setActualizado(LocalDateTime.now());
                Aprobacion aceptada = repositorioAprobacion.saveAndFlush(aprobacion);
                List<Vacacion> lista = repositorioVacacion.findByAprobacion(aceptada);
                if (CollectionUtils.isNotEmpty(lista)) {
                    for (Vacacion v : lista) {
                        v.setActualizado(aceptada.getActualizado());
                        repositorioVacacion.save(v);
                    }
                    ok = true;
                } else {
                    throw new AngelException("La lista de dias no puede ser vacia");
                }
            }
        }
        return ok;
    }

    @Transactional
    public boolean rechazar(Empleado empleado, String motivo) {
        boolean ok = false;
        if (empleado != null && StringUtils.isNotBlank(motivo)) {
            Aprobacion aprobacion = repositorioAprobacion.findByMotivoIgnoreCaseAndEmpleado(motivo, empleado);
            if (aprobacion != null) {
                aprobacion.setAceptado(false);
                aprobacion.setActualizado(LocalDateTime.now());
                Aprobacion rechazada = repositorioAprobacion.saveAndFlush(aprobacion);
                List<Vacacion> lista = repositorioVacacion.findByAprobacion(rechazada);
                if (CollectionUtils.isNotEmpty(lista)) {
                    for (Vacacion v : lista) {
                        repositorioVacacion.delete(v);
                    }
                    ok = true;
                } else {
                    throw new AngelException("La lista de dias no puede ser vacia");
                }
            }
        }
        return ok;
    }
}
