package cl.angel.demo.modelo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
/**
 *
 * @author angelexperti
 */
@Entity
@Table(name = "vacaciones")
public class Vacacion extends Angel {

    @ManyToOne
    @JoinColumn(name = "aprobacion_fk", referencedColumnName = "pk", nullable = false)
    private Aprobacion aprobacion = null;
    
    @Column(name = "fecha_libre", nullable = false)
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
