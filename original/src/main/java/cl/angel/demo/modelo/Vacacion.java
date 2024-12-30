package cl.angel.demo.modelo;

import java.time.LocalDate;

/**
 *
 * @author angelexperti
 */
public class Vacacion extends Angel {

    private Aprobacion aprobacion = null;
    private LocalDate fechaLibre = null;

    public Aprobacion getAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(Aprobacion aprobacion) {
        this.aprobacion = aprobacion;
    }

    public LocalDate getFechaLibre() {
        return fechaLibre;
    }

    public void setFechaLibre(LocalDate fechaLibre) {
        this.fechaLibre = fechaLibre;
    }

    @Override
    public int compareTo(Angel o) {
        if (o instanceof Vacacion) {
            return fechaLibre.compareTo(((Vacacion) o).getFechaLibre());
        } else {
            return super.compareTo(o);
        }
    }
}
