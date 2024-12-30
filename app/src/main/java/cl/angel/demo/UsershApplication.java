package cl.angel.demo;

import cl.angel.demo.administrador.AdministradorEmpleado;
import cl.angel.demo.administrador.AdministradorAprobacion;
import cl.angel.demo.administrador.AdministradorVacacion;

import cl.angel.demo.modelo.Aprobacion;
import cl.angel.demo.modelo.Empleado;
import cl.angel.demo.modelo.Vacacion;
import cl.angel.demo.utils.FechaUtils;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        final String motivo = "Días administrativos";
        final long rut = 12111111L;

        System.out.println("Empleado");
        Empleado emp = administradorEmpleado.consultar(rut);
        if (emp == null) {
            emp = new Empleado();
            emp.setRut(rut);
            emp.setApellidos("Perez");
            emp.setNombres("Juan");
            emp.setContratacion(LocalDate.now());
        } else {
            emp.setActualizado(LocalDateTime.now());
        }
        Empleado guardado = administradorEmpleado.guardar(emp);
        System.out.println(String.format("Se guardo con id %d y actualizacion %s", guardado.getId(), guardado.getActualizado()));

        //////////////////////////////////////////////
        
        System.out.println("Aprobacion");
        Aprobacion aprobacion = administradorAprobacion.consultar(motivo, guardado);
        if (aprobacion == null) {
            Aprobacion solicitud = new Aprobacion();
            solicitud.setAceptado(false);
            solicitud.setEmpleado(guardado);
            solicitud.setMotivo(motivo);
            aprobacion = administradorAprobacion.guardar(solicitud);
        }

        List<Aprobacion> lista = administradorAprobacion.consultar(guardado);
        if (CollectionUtils.isNotEmpty(lista)) {
            for (Aprobacion apr : lista) {
                System.out.println(String.format("Hay una solicitud %s para %s %s",
                        apr.getMotivo(),
                        apr.getEmpleado().getNombres(),
                        apr.getEmpleado().getApellidos()
                )
                );
            }
        } else {
            System.out.println("No hay aprobaciones");
        }

        //////////////////////////////////////////////
        
        System.out.println("Aprobacion");
        String motivoAleatoreo = RandomStringUtils.randomAlphabetic(7);
        administradorVacacion.solicitar(guardado, motivoAleatoreo, FechaUtils.fechasTrabajables(LocalDate.now(), LocalDate.now().plusDays(7)));

        List<Vacacion> vacacion = administradorVacacion.consultar(guardado);
        if (CollectionUtils.isNotEmpty(vacacion)) {
            for (Vacacion vaca : vacacion) {
                System.out.println(String.format("%s %s solicitó vacaciones para el día %s para %s",
                        vaca.getAprobacion().getEmpleado().getNombres(),
                        vaca.getAprobacion().getEmpleado().getApellidos(),
                        vaca.getFechaLibre(),
                        vaca.getAprobacion().getMotivo()
                )
                );
            }
        }

        if (Instant.now().getEpochSecond() % 2 == 0) {
            boolean acp = administradorVacacion.aceptar(guardado, motivoAleatoreo);
            System.out.println(String.format("%s", acp ? "Solicitud aceptada" : "Solicitud NO aceptada"));
        } else {
            boolean rcz = administradorVacacion.rechazar(guardado, motivoAleatoreo);
            System.out.println(String.format("%s", rcz ? "Solicitud rechazada" : "Solicitud NO rechazada"));
        }
    }

    /*
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
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
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
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
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
                            
                        //Solicitar RUT por terminal
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
                        long rutEmp = sn.nextLong();
                        sn.nextLine(); // Consumir el salto de línea después de nextLong()
                        
                        //Solicitar MOTIVO por terminal
                        System.out.println("Ingrese el motivo de las vacaciones:");
                        String detalleMotivo = sn.nextLine();
                        
                        Empleado seba = repoEmpleado.lectura(rutEmp);
                        if(seba == null){
                            System.out.println("El empleado no existe...");
                        } else {
                            seba = repoEmpleado.lectura(rutEmp);              
                            Aprobacion aprobaciones = repoAprobacion.lectura(seba, detalleMotivo);
                        
                            if(aprobaciones.getMotivo().equals(detalleMotivo)){
                                
                                //Solicitar Aprobación o Rechazo por terminal
                                System.out.println("Aprobar Vacaciones (true) - Rechazar Vacaciones (false)");
                                boolean answ = sn.nextBoolean();
                                
                                if(answ == true){
                                    if(aprobaciones.getMotivo().equals(detalleMotivo))
                                    {
                                        //Aprueba y actualiza tabla aprobación
                                        aprobaciones.setActualizado(LocalDateTime.now());
                                        aprobaciones.setAceptado(answ);
                                        repoAprobacion.actualizacion(aprobaciones);
                                        
                                        //Actualiza vacaciones
                                        List<Vacacion> vacaciones = repoVacacion.obtenerVacaciones(seba);
                                        if (CollectionUtils.isNotEmpty(vacaciones)) {
                                            for(Vacacion v : vacaciones){
                                                System.out.println("AAA");
                                                String aa = v.getAprobacion().getMotivo();
                                                
                                                if(aa.equals(detalleMotivo))
                                                {        
                                                    v.setActualizado(LocalDateTime.now());
                                                    repoVacacion.actualizacion(v);
                                                }
                                            }
                                            
                                        }
                                        System.out.println("Vacaciones aceptadas...");
                                    }                       
                                } else {
                                    //Rechaza y actualiza tabla aprobación
                                    aprobaciones.setActualizado(LocalDateTime.now());
                                    aprobaciones.setAceptado(answ);
                                    repoAprobacion.actualizacion(aprobaciones);
                                    
                                    //Elimina las vacaciones
                                    List<Vacacion> vacaciones = repoVacacion.obtenerVacaciones(seba);
                                    if (CollectionUtils.isNotEmpty(vacaciones)) {
                                        for(Vacacion v : vacaciones){
                                            if(v.getAprobacion().getMotivo().equals(detalleMotivo))
                                            {        
                                                repoVacacion.eliminar(v);     
                                            }
                                        }
                                    }
                                    System.out.println("Vacaciones rechazadas");
                                }
                            } else {
                                System.out.println("No hay vacaciones con ese motivo");
                            }            
                        }                                                                    
                    }
                    case 4 -> { 
                        System.out.println("Mostrar vacaciones de un empleado");
                        
                        //Solicitar RUT por terminal (Long)
                        System.out.println("Ingrese el RUT del empleado (sin puntos, sin guión y sin dígito verificador):");
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
                                System.out.println("El empleado no tiene vacaciones disponibles...");
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
     */
}
