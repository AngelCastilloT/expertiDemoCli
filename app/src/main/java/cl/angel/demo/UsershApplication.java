package cl.angel.demo;
import cl.angel.demo.administrador.AdministradorEmpleado;
import cl.angel.demo.administrador.AdministradorAprobacion;
import cl.angel.demo.administrador.AdministradorVacacion;
import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.modelo.Vacacion;
import cl.angel.demo.utils.FechaUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 *
 * @author angelexperti
 */
@SpringBootApplication
public class UsershApplication implements CommandLineRunner {

    private final AdministradorEmpleado administradorEmpleado;
    private final AdministradorAprobacion administradorAprobacion;
    private final AdministradorVacacion administradorVacacion;

    @Autowired
    public UsershApplication(AdministradorEmpleado administradorEmpleado, AdministradorAprobacion administradorAprobacion, AdministradorVacacion administradorVacacion) {
        this.administradorEmpleado = administradorEmpleado;
        this.administradorAprobacion = administradorAprobacion;
        this.administradorVacacion = administradorVacacion;
    }

    public static void main(String[] args) {
        SpringApplication.run(UsershApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        int opcion;
        while (!salir) {
            System.out.println("1. Agregar Empleado");
            System.out.println("2. Solicitar Vacaciones");
            System.out.println("3. Aceptar o Rechazar Vacaciones");
            System.out.println("4. Mostrar Empleados");
            System.out.println("5. Mostrar Solicitudes de Vacaciones de un Empleado");
            System.out.println("6. Mostrar Vacaciones de un Empleado");
            System.out.println("7. Salir");
            try {
                System.out.println("Escribe una de las opciones");
                opcion = sn.nextInt();
                switch (opcion) {
                    case 1 -> {
                        System.out.println("\nAgregar Empleado");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado emp = administradorEmpleado.consultar(rutEmp);
                        if (emp == null) {
                            
                            //Solicitar datos por terminal (nomEmp (String), appeEmp (String), contraEmp (LocalDate)
                            System.out.println("Ingrese los nombres del empleado:");
                            String nomEmp = sn.nextLine();

                            System.out.println("Ingrese los apellidos del empleado:");
                            String appeEmp = sn.nextLine();
                            
                            System.out.println("Ingrese la fecha de contratación (YYYY-MM-DD):");
                            String fechaContratacionStr = sn.nextLine();
                            LocalDate contraEmp;
                            try {
                                // Formateador para el formato YYYY-MM-DD
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                contraEmp = LocalDate.parse(fechaContratacionStr, formatter);
                            } catch (Exception e) {
                                System.out.println("Fecha inválida. Asegúrese de usar el formato YYYY-MM-DD.");
                                break;
                            }
                            emp = new Empleado();
                            emp.setRut(rutEmp);
                            emp.setApellidos(appeEmp);
                            emp.setNombres(nomEmp);
                            emp.setContratacion(contraEmp);
                            Empleado guardado = administradorEmpleado.guardar(emp);
                            
                            System.out.println(String.format("El empleado %s %s se guardó correctamente \n", guardado.getNombres(), guardado.getApellidos()));
                        } else {
                            System.out.println(String.format("El empleado con rut: %d ya existe... \n", rutEmp));
                        }
                    }
                    case 2 -> {  
                        System.out.println("\nSolicitar Vacaciones");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado emp = administradorEmpleado.consultar(rutEmp);
                        if (emp != null) {
                            System.out.println("Ingrese el motivo de sus vacaciones: ");
                            String motivo = sn.nextLine();
                            
                            Aprobacion aprobacion = administradorAprobacion.consultar(motivo, emp);
                            if (aprobacion == null) {
                                //Fecha DESDE validada
                                System.out.println("Ingrese el inicio de las vacaciones solicitadas (YYYY-MM-DD): ");
                                String fechaDesdeStr = sn.nextLine();
                                LocalDate desde;
                                try {
                                    // Formateador para el formato YYYY-MM-DD
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    desde = LocalDate.parse(fechaDesdeStr, formatter);
                                } catch (Exception e) {
                                    System.out.println("Fecha inválida. Asegúrese de usar el formato YYYY-MM-DD. \n");
                                    break;
                                }

                                //Fecha HASTA validada
                                System.out.println("Ingrese el termino de las vacaciones solicitadas (YYYY-MM-DD): ");
                                String fechaHastaStr = sn.nextLine();
                                LocalDate hasta;
                                try {
                                    // Formateador para el formato YYYY-MM-DD
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    hasta = LocalDate.parse(fechaHastaStr, formatter);
                                } catch (Exception e) {
                                    System.out.println("Fecha inválida. Asegúrese de usar el formato YYYY-MM-DD.\n");
                                    break;
                                }
                                
                                //Solicitud de vacaciones
                                administradorVacacion.solicitar(emp, motivo, FechaUtils.fechasTrabajables(desde, hasta));
                                
                                System.out.println(String.format("Vacaciones solicitas correctamente"));
                                System.out.println(String.format("Vacaciones solicitas para empleado: %d con motivo %s, entre los días %s - %s \n", 
                                        rutEmp, 
                                        motivo, 
                                        fechaDesdeStr, 
                                        fechaHastaStr));
                            } else {
                                System.out.println(String.format("Vacaciones no ingresadas"));
                                System.out.println(String.format("El empleado con rut: %d ya solicitó anteriormente unas vacaciones con el motivo: %s \n", 
                                        rutEmp, 
                                        motivo));
                            }
                        } else {
                            System.out.println(String.format("No hay empleado con rut: %d \n", rutEmp));
                        }
                    }
                    case 3 -> {
                        System.out.println("\nAceptar o Rechazar Vacaciones");
                            
                        //Solicitar RUT por terminal
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado emp = administradorEmpleado.consultar(rutEmp);
                        if(emp != null){
                            
                            //Solicitar MOTIVO por terminal
                            System.out.println("Ingrese el motivo de las vacaciones:");
                            String motivo = sn.nextLine();
                            
                            Aprobacion aprobacion = administradorAprobacion.consultar(motivo, emp);
                            if (aprobacion != null) {
                                //Solicitar Aprobación o Rechazo por terminal
                                System.out.println("Aprobar Vacaciones (true) - Rechazar Vacaciones (false)");
                                boolean answ = sn.nextBoolean();

                                if (answ == true) {
                                    boolean acp = administradorVacacion.aceptar(emp, motivo);
                                    System.out.println(String.format("%s", acp ? "Solicitud aceptada\n" : "Solicitud NO aceptada\n"));
                                } else {
                                    boolean rcz = administradorVacacion.rechazar(emp, motivo);
                                    System.out.println(String.format("%s", rcz ? "Solicitud rechazada\n" : "Solicitud NO rechazada\n"));
                                }
                            } else {
                                System.out.println(String.format("No existe una solicitud de vacaciones del empleado %d con motivo %s \n", rutEmp, motivo));
                            }
                        } else {
                            System.out.println("\n");
                            System.out.println(String.format("El empleado con rut: %d no existe \n", rutEmp));
                        }                                                                         
                    }
                    case 4 -> {
                        System.out.println("\nMostrar Empleados");
                        List<Empleado> empleado = administradorEmpleado.consultarTodos();
                        if(CollectionUtils.isNotEmpty(empleado)){
                            for(Empleado p : empleado){
                                System.out.println(String.format("Empleado %s %s, rut: %d, contratado: %s", 
                                        p.getNombres(),
                                        p.getApellidos(),
                                        p.getRut(),
                                        p.getContratacion()));
                            }
                            System.out.println("");
                        } else {
                            System.out.println("No hay empleados registrados... \n");
                        }
                    }
                    case 5 -> {
                        System.out.println("\nMostrar Solicitudes de Vacaciones de un Empleado");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado emp = administradorEmpleado.consultar(rutEmp);
                        if(emp != null){
                            List<Aprobacion> lista = administradorAprobacion.consultar(emp);
                            if (CollectionUtils.isNotEmpty(lista)) {
                                for (Aprobacion apr : lista) {
                                    System.out.println(String.format("El empleado %s %s solicitó Vacaciones para: %s",
                                            apr.getEmpleado().getNombres(),
                                            apr.getEmpleado().getApellidos(),
                                            apr.getMotivo()));
                                }
                                System.out.println("");
                            } else {
                                System.out.println("No hay solicitudes\n");
                            }
                        } else {
                            System.out.println(String.format("El empleado con rut: %d no existe\n", rutEmp));
                        }
                        
                        
                    }
                    case 6 -> { 
                        System.out.println("\nMostrar Vacaciones de un Empleado");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        Empleado emp = administradorEmpleado.consultar(rutEmp);
                        if(emp != null){
                            List<Vacacion> vacacion = administradorVacacion.consultar(emp);
                            if (CollectionUtils.isNotEmpty(vacacion)) {
                                for (Vacacion vaca : vacacion) {
                                    boolean estado = vaca.getAprobacion().isAceptado();
                                    if(estado == true){
                                        System.out.println(String.format("%s %s solicitó vacaciones para el día %s para %s, estado: Aprobado",
                                                vaca.getAprobacion().getEmpleado().getNombres(),
                                                vaca.getAprobacion().getEmpleado().getApellidos(),
                                                vaca.getFechaLibre(),
                                                vaca.getAprobacion().getMotivo()));
                                    } else {
                                        System.out.println(String.format("%s %s solicitó vacaciones para el día %s para %s, estado: Rechazado",
                                                vaca.getAprobacion().getEmpleado().getNombres(),
                                                vaca.getAprobacion().getEmpleado().getApellidos(),
                                                vaca.getFechaLibre(),
                                                vaca.getAprobacion().getMotivo()));
                                    }
                                }
                                System.out.println("");
                            } else {
                                System.out.println(String.format("El empleado %s %s con rut: %d no tiene vacaciones\n", emp.getNombres(), emp.getApellidos(), emp.getRut()));
                            }
                        } else {
                            System.out.println(String.format("El empleado con rut: %d no existe \n", rutEmp));
                        }
                    }
                    case 7 -> salir = true;
                    default -> System.out.println("Solo números entre 1 y 6");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número");
                sn.next();
            }
        }
    }
}
