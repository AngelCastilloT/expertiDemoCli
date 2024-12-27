package cl.angel.demo;

import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Vacacion;

import cl.angel.demo.repositorio.RepoAprobacion;
import cl.angel.demo.repositorio.RepoEmpleado;
import cl.angel.demo.repositorio.RepoVacacion;
import cl.angel.demo.utils.FechaUtils;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsershApplication implements CommandLineRunner {

    private final RepoEmpleado repoEmpleado;
    private final RepoAprobacion repoAprobacion;
    private final RepoVacacion repoVacacion;

    @Autowired
    public UsershApplication(RepoEmpleado repoEmpleado, RepoAprobacion repoAprobacion, RepoVacacion repoVacacion) {
        this.repoEmpleado = repoEmpleado;
        this.repoAprobacion = repoAprobacion;
        this.repoVacacion = repoVacacion;
    }

    public static void main(String[] args) {
        SpringApplication.run(UsershApplication.class, args);
    }

    private void solicitarVacaciones(Empleado empleado, String motivo, List<LocalDate> dias) throws SQLException {
        if (dias != null && !dias.isEmpty()) {
            Aprobacion aprobacion = repoAprobacion.lectura(empleado, motivo);
            if (aprobacion == null) {
                Aprobacion solicitud = new Aprobacion();
                solicitud.setAceptado(false);
                solicitud.setEmpleado(empleado);
                solicitud.setMotivo(motivo);
                repoAprobacion.creacion(solicitud);
                aprobacion = repoAprobacion.lectura(empleado, motivo);
                for (LocalDate dia : dias) {
                    Vacacion v = new Vacacion();
                    v.setAprobacion(aprobacion);
                    v.setFechaLibre(dia);
                    repoVacacion.creacion(v);
                }
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {

        final long rutSeba = 15997886L;
        final String motivo = "Flojeo";

//        LocalDate desde = LocalDate.of(2024, Month.NOVEMBER, 29);
//        final LocalDate hasta = LocalDate.of(2024, Month.DECEMBER, 6);
        LocalDate desde = LocalDate.of(2024, Month.DECEMBER, 29);
        final LocalDate hasta = LocalDate.of(2025, Month.JANUARY, 5);

        Empleado seba = repoEmpleado.lectura(rutSeba);
        if (seba == null) {
            Empleado emp = new Empleado();
            emp.setRut(rutSeba);
            emp.setApellidos("Salazar Molina");
            emp.setNombres("Sebastián Alexis");
            emp.setContratacion(LocalDate.of(2010, Month.JANUARY, 2));
            repoEmpleado.creacion(emp);
            seba = repoEmpleado.lectura(rutSeba);
        }

        List<LocalDate> fechasTrabajables = FechaUtils.fechasTrabajables(desde, hasta);
        solicitarVacaciones(seba, motivo, fechasTrabajables);
        List<Vacacion> vacaciones = repoVacacion.obtenerVacaciones(seba);
        if (CollectionUtils.isNotEmpty(vacaciones)) {
            for (Vacacion v : vacaciones) {
                System.out.println(String.format("%s %s pidio el día %s con motivo %s y resultado %s",
                        v.getAprobacion().getEmpleado().getNombres(),
                        v.getAprobacion().getEmpleado().getApellidos(),
                        v.getFechaLibre(),
                        v.getAprobacion().getMotivo(),
                        v.getAprobacion().isAceptado() ? "Aprobado" : "Rechazado")
                );
            }
        }
    }
}
