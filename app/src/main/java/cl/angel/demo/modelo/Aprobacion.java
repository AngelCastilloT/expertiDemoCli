package cl.angel.demo.modelo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
/**
 *
 * @author angelexperti
 */
@Entity
@Table(name = "aprobaciones")
public class Aprobacion extends Angel {

    @ManyToOne
    @JoinColumn(name = "empleado_fk", referencedColumnName = "pk", nullable = false)
    private Empleado empleado = null;

    @Column(name = "motivo", nullable = false)
    private String motivo = null;

    @Column(name = "aceptado", nullable = false)
    private boolean aceptado = false;

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }
}
