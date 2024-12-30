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
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
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

        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        int opcion; //Guardaremos la opcion del usuario
 
        while (!salir) {
 
            System.out.println("1. Agregar Empleado");
            System.out.println("2. Solicitar Vacaciones");
            System.out.println("3. Aceptar o Rechazar Vacaciones");
            System.out.println("4. Mostrar vacaciones de un empleado");
            System.out.println("5. Salir");
 
            try {
 
                System.out.println("Escribe una de las opciones");
                opcion = sn.nextInt();
 
                switch (opcion) {
                    case 1 -> {
                        System.out.println("Agregar Empleado");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos ni guión):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado seba = repoEmpleado.lectura(rutEmp);
                        if (seba == null) {
                            
                            //Solicitar datos por terminal (nomEmp (String), appeEmp (String), contraEmp (LocalDate)
                            System.out.println("Ingrese los nombres del empleado:");
                            String nomEmp = sn.nextLine();

                            System.out.println("Ingrese los apellidos del empleado:");
                            String appeEmp = sn.nextLine();
                            
                            System.out.println("Ingrese la fecha de contratación (YY-MM-DD):");
                            String fechaContratacionStr = sn.nextLine();
                            LocalDate contraEmp;
                            try {
                                // Formateador para el formato YY-MM-DD
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                contraEmp = LocalDate.parse(fechaContratacionStr, formatter);
                            } catch (Exception e) {
                                System.out.println("Fecha inválida. Asegúrese de usar el formato YY-MM-DD.");
                                break;
                            }
                            
                            Empleado emp = new Empleado();
                            emp.setRut(rutEmp);
                            emp.setNombres(nomEmp);
                            emp.setApellidos(appeEmp);
                            emp.setContratacion(contraEmp);
                            repoEmpleado.creacion(emp);
                            //seba = repoEmpleado.lectura(rutEmp);
                            System.out.println("Empleado ingresado correctamente...");
                        } else {
                            System.out.println("El empleado ya existe...");
                        }
                        
                    }
                    case 2 -> {  
                        System.out.println("Solicitar Vacaciones");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos ni guión):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado seba = repoEmpleado.lectura(rutEmp);
                        if(seba == null)
                        {
                            System.out.println("El empleado no existe...");
                        } else {
                            //Si el Empleado existe: 
                            
                            //Fecha DESDE validada
                            System.out.println("Ingrese el inicio de las vacaciones solicitadas: ");
                            String fechaDesdeStr = sn.nextLine();
                            LocalDate desde;
                            try {
                                // Formateador para el formato YY-MM-DD
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                desde = LocalDate.parse(fechaDesdeStr, formatter);
                            } catch (Exception e) {
                                System.out.println("Fecha inválida. Asegúrese de usar el formato YY-MM-DD.");
                                break;
                            }
                            
                            //Fecha HASTA validada
                            System.out.println("Ingrese el termino de las vacaciones solicitadas: ");
                            String fechaHastaStr = sn.nextLine();
                            LocalDate hasta;
                            try {
                                // Formateador para el formato YY-MM-DD
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                hasta = LocalDate.parse(fechaHastaStr, formatter);
                            } catch (Exception e) {
                                System.out.println("Fecha inválida. Asegúrese de usar el formato YY-MM-DD.");
                                break;
                            }
                            
                            seba = repoEmpleado.lectura(rutEmp);
                            List<LocalDate> fechasTrabajables = FechaUtils.fechasTrabajables(desde, hasta);
                            
                            //MOTIVO solicitado
                            System.out.println("Ingrese el motivo de las vacaciones:");
                            String motivo = sn.nextLine();
                            
                            solicitarVacaciones(seba, motivo, fechasTrabajables);
                            System.out.println("Solicitud de Vacaciones ingresada satisfactoriamente...");       
                        }  
                    }
                    case 3 -> {
                        System.out.println("Aceptar o Rechazar Vacaciones");
                        
                        System.out.println("Aprobaciones disponibles...");
                            
     
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos ni guión):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        System.out.println("Ingrese el motivo de las vacaciones:");
                        String detalleMotivo = sn.nextLine();
                        
                        Empleado seba = repoEmpleado.lectura(rutEmp);
                        if(seba == null){
                            System.out.println("El empleado no existe...");
                        } else {
                            seba = repoEmpleado.lectura(rutEmp);              
                            Aprobacion aprobaciones = repoAprobacion.lectura(seba, detalleMotivo);
                        
                            if(aprobaciones.getMotivo().equals(detalleMotivo)){
                                System.out.println("Aprobar Vacaciones (true) - Rechazar Vacaciones (false)");
                                boolean answ = sn.nextBoolean();
                                
                                if(answ == true){
                                    if(aprobaciones.getMotivo().equals(detalleMotivo))
                                    {
                                        aprobaciones.setAceptado(true); 
                                    }                       
                                } else {
                                    List<Vacacion> vacaciones = repoVacacion.obtenerVacaciones(seba);
                                    if (CollectionUtils.isNotEmpty(vacaciones)) {
                                        for(Vacacion vv : vacaciones){
                                            if(vv.getAprobacion().getMotivo().equals(detalleMotivo))
                                            {
                                                repoVacacion.eliminar(vv);
                                                
                                            }
                                        }
                                    }
                                    
                                    System.out.println("Vacaciones rechazadas");
                                }
                                aprobaciones.setActualizado(LocalDateTime.now());
                            } else {
                                System.out.println("No hay vacaciones con ese motivo");
                                
                            }            
                        }                                                                    
                    }
                    case 4 -> { 
                        System.out.println("Mostrar vacaciones de un empleado");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos ni guión):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado seba = repoEmpleado.lectura(rutEmp);
                        if(seba == null){
                            System.out.println("El empleado no existe...");
                        } else {
                            seba = repoEmpleado.lectura(rutEmp);
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
                            } else {
                                System.out.println("El empleado no tiene vacaciones...");
                            }
                        }  
                    }
                    case 5 -> salir = true;
                    default -> System.out.println("Solo números entre 1 y 5");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número");
                sn.next();
            }
        }
  
    }
}
