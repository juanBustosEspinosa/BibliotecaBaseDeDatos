package Biblioteca;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public static Scanner sc = new Scanner(System.in);
    private static String jpql = "FROM Usuario u WHERE u.nombre = ?1 AND u.password = ?2";
    private static DAOGenericoBiblioteca daoEjemplar = new DAOGenericoBiblioteca(Ejemplar.class);
    private static DAOGenericoBiblioteca daoLibro = new DAOGenericoBiblioteca(Libro.class);
    private static DAOGenericoBiblioteca daoPrestamo = new DAOGenericoBiblioteca(Prestamo.class);
    private static DAOGenericoBiblioteca daoUsuario = new DAOGenericoBiblioteca(Usuario.class);
    private static String busquedaid= "FROM Prestamo p WHERE p.usuario.id = ?1 AND p.ejemplar.id = ?2";
    private static String busquedaISBN = "FROM Ejemplar e WHERE e.isbn = ?1";
    private static String busquedaidUsuarioPrestamo= "FROM Prestamo p WHERE p.usuario.id = ?1";
    private static String BusDNI = "FROM Usuario u WHERE u.dni = ?1";

    public void lista(){

        boolean registro = true;
        while (registro){
            System.out.println("Dime el nombre de tu usuario");
            String nombre = sc.nextLine();
            System.out.println("Dime la contraseña");
            String contrasena = sc.nextLine();

            Usuario usuario =(Usuario) daoUsuario.findByQuery(jpql, nombre, contrasena);

            if (usuario != null){

                switch (usuario.getTipo()){
                    case "administrador": listaAdministrador();
                        break;
                    case "normal": listaNormal(usuario);
                        break;
                }



            } else {
                System.out.println("Usuario no encontrado");
            }

        }




    }


    //Lista del Administrador

    public boolean listaAdministrador(){
        boolean administrador = true;
        while (administrador){
        System.out.println("Dime que quieres hacer Sobre cada tabla:");
        System.out.println("1. Ejemplares");
        System.out.println("2. Libros");
        System.out.println("3. Prestamo");
        System.out.println("4. Usuario");
        System.out.println("5. Salir");
        int opcion = sc.nextInt();

        switch (opcion){
            case 1: administrador = opcionesEjemplar();
            break;
            case 2: administrador = opcionesLibro();
            break;
            case 3: opcionesPrestamo();
            break;
            case 4: opcionesUsuario();
            break;
            case 5: administrador = false;
            break;
            default:
                System.out.println("Opcion no valida");
        }
        }
        return false;
    }

//Ejemplar
    public boolean opcionesEjemplar(){
        boolean ejemplares = true;
        while (ejemplares){
            System.out.println("Dime que quieres hacer con Un ejemplar");
            System.out.println("1. Añadir");
            System.out.println("2. Stock Disponible");
            int opcion = sc.nextInt();
            switch (opcion){
                case 1:
                    System.out.println("ISBN del libro");
                    String isbn = sc.nextLine();
                    sc.nextLine();
                    System.out.println("Estado Disponible, Prestado o Dañado");
                    String estado = sc.nextLine();
                    Libro libro =(Libro) daoLibro.getClase(isbn);
                    if (libro != null && (estado.equals("Disponible") || estado.equals("Prestado") || estado.equals("Dañado")))
                    {
                        daoEjemplar.addClase(new Ejemplar(libro,estado));
                        return true;
                    }
                    break;
                    case 2:
                        String jsql= "FROM Ejemplar e WHERE e.estado = ?1";
                        System.out.println(daoEjemplar.findByQueryAll(jsql, "Disponible"));
                        return true;

                    default:
                        System.out.println("Opcion no valida");
                        return true;
            }
        }
        return true;
    }



    //Opciones Libros
public boolean opcionesLibro(){
    System.out.println("dime el ISBN del libro");
    sc.nextLine();
    String isbn = sc.nextLine();

    System.out.println("Dime el titulo");
    String titulo = sc.nextLine();
    System.out.println("dime el autor del libro");
    String autor = sc.nextLine();
    daoLibro.addClase(new Libro(isbn,titulo,autor));
        return true;
}

//Opciones Prestamo
public boolean opcionesPrestamo(){
        boolean prestamo = true;
    System.out.println("Dime lo que quieres hacer:");
    System.out.println("1. Registrar");
    System.out.println("2. Devolver");
    int opcion = sc.nextInt();
    switch (opcion){
        case 1:
            System.out.println("Dime el DNI");
            sc.nextLine();
            String dni = sc.nextLine();
            System.out.println("Dime el Isbn");
            String isbn = sc.nextLine();
            Usuario usuario1 = (Usuario) daoUsuario.findByQuery(BusDNI,dni);
            Libro libro = (Libro) daoLibro.getClase(isbn);
            List<Ejemplar> ejemplares = daoEjemplar.findByQueryAll(busquedaISBN,libro);
            Ejemplar ejemnuevo = null;
            List<Prestamo> pres =daoPrestamo.findByQueryAll(busquedaidUsuarioPrestamo,usuario1.getId());
            int contador= 0;
            for (Prestamo p: pres){
                if (p.getFechaDevolucion() == null){
                    contador++;
                }
            }
            if (contador <3 || !(usuario1.getPenalizacionHasta().isAfter(LocalDate.now()))) {


                for (Ejemplar e : ejemplares) {
                    if (e.getEstado().equals("Disponible")) {
                        ejemnuevo = e;
                    }
                }
                if (ejemnuevo != null) {
                    daoPrestamo.addClase(new Prestamo(usuario1, ejemnuevo, LocalDate.now(), null));
                    ejemnuevo.setEstado("Prestado");
                    daoEjemplar.updateClase(ejemnuevo);
                } else {
                    System.out.println("El libro no existe");
                }
            }else {
                System.out.println("El usuario no puede obtener mas prestamos ");
            }
            return true;
            case 2:
                System.out.println("Dime el DNI");
                sc.nextLine();
                String dni2 = sc.nextLine();
                System.out.println("Dime el Isbn");
                String isbn2 = sc.nextLine();
                Usuario usuario2 = (Usuario) daoUsuario.findByQuery(BusDNI,dni2);
                Libro libro2 = (Libro) daoLibro.getClase(isbn2);
                List<Ejemplar> ejemplares2 = daoEjemplar.findByQueryAll(busquedaISBN,libro2);
//                Ejemplar ejemnuevo2 = null;
                Prestamo prestamo2 = null;

                for (Ejemplar e : ejemplares2) {
                    if (e.getEstado().equals("Prestado")) {
                        prestamo2 = (Prestamo) daoPrestamo.findByQuery(busquedaid,usuario2.getId(),e.getId());
                        if (prestamo2 != null) {
                            break;
                        }
                    }
                }

                if (usuario2 != null && libro2 != null) {
//                List<Ejemplar> ejemplares2 = daoEjemplar.findByQueryAll(busquedaISBN,libro2);
//                    Prestamo prestamo2 = (Prestamo) daoPrestamo.findByQuery(busquedaid, usuario2.getId(), libro2);
                    prestamo2.setFechaDevolucion(LocalDate.now());
                    daoPrestamo.updateClase(prestamo2);
                    long diasEntre = ChronoUnit.DAYS.between(prestamo2.getFechaInicio(), prestamo2.getFechaDevolucion());
                    if (diasEntre > 15) {
                        usuario2.setPenalizacionHasta(LocalDate.now().plusDays(40));
                        daoUsuario.updateClase(usuario2);
                    }
                    Ejemplar ejm = (Ejemplar) daoEjemplar.getClaseId(prestamo2.getEjemplar().getId());
                    ejm.setEstado("Disponible");
                    daoEjemplar.updateClase(ejm);
                } else {
                    System.out.println("El usuario no existe o el Libro");
                }
                return true;



    }



        return true;
}

//Opciones Usuario
    public boolean opcionesUsuario(){
        boolean usuario = true;
        System.out.println("Dime lo que quieres hacer:");
        System.out.println("1. Registrar");
        System.out.println("Si quieres Devolver vete a prestamos");
        int opcion = sc.nextInt();
        switch (opcion){
            case 1:
                System.out.println("Dime el Dni");
                String dni = sc.nextLine();
                System.out.println("Dime el nombre");
                String nombre = sc.nextLine();
                System.out.println("Dime el email");
                String email = sc.nextLine();
                System.out.println("Dime el password");
                String password = sc.nextLine();
                System.out.println("Dime el tipo Normal o Administrador");
                String tipo = sc.nextLine();
                daoUsuario.addClase(new Usuario(dni,nombre,email,password,tipo,null));
                return true;
                    default:
                        return true;
                    //
        }

    }




//Lista del Usuario Normal
    public boolean listaNormal(Usuario usuario){

        System.out.println(daoPrestamo.findByQueryAll(busquedaidUsuarioPrestamo,usuario.getId()));

        return true;
    }



}
