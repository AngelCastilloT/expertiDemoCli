package cl.angel.demo.modelo;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 *
 * @author angelexperti
 */
@MappedSuperclass
public class Angel implements Comparable<Angel> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private Long id = null;

    @Column(name = "creado", nullable = false, updatable = false)
    private LocalDateTime creado = LocalDateTime.now();

    @Column(name = "actualizado", nullable = false)
    private LocalDateTime actualizado = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreado(LocalDateTime creado) {
        this.creado = creado;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    public LocalDateTime getActualizado() {
        return actualizado;
    }

    public void setActualizado(LocalDateTime actualizado) {
        this.actualizado = actualizado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Angel other = (Angel) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Fila " + id;
    }

    @Override
    public int compareTo(Angel o) {
        int comparacion = 1;
        if (o != null) {
            comparacion = creado.compareTo(o.getCreado());
        }
        return comparacion;
    }
}
