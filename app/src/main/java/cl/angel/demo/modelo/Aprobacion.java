package cl.angel.demo.modelo;

/**
 *
 * @author angelexperti
 */
public class Aprobacion extends Angel {

    private Empleado empleado = null;
    private String motivo = null;
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
