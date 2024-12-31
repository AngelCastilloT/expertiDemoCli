package cl.angel.demo.modelo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
/**
 *
 * @author angelexperti
 */
@Entity
@Table(name = "empleados")
public class Empleado extends Angel {

    @Column(name = "rut", nullable = false, unique = true)
    private Long rut = null;

    @Column(name = "nombres", nullable = false)
    private String nombres = null;

    @Column(name = "apellidos", nullable = false)
    private String apellidos = null;

    @Column(name = "contratacion", nullable = false)
    private LocalDate contratacion = null;

    public Long getRut() {
        return rut;
    }

    public void setRut(Long rut) {
        this.rut = rut;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getContratacion() {
        return contratacion;
    }

    public void setContratacion(LocalDate contratacion) {
        this.contratacion = contratacion;
    }
}
