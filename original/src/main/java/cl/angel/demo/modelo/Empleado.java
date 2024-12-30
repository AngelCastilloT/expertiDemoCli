package cl.angel.demo.modelo;

import java.time.LocalDate;

/**
 *
 * @author angelexperti
 */
public class Empleado extends Angel {

    private Long rut = null;
    private String nombres = null;
    private String apellidos = null;
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
