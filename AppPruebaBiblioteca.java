package Biblioteca;

public class AppPruebaBiblioteca {


    public static void main(String[] args) {
//        DAOGenericoBiblioteca daoEjemplar = new DAOGenericoBiblioteca(Ejemplar.class);
//        DAOGenericoBiblioteca daoLibro = new DAOGenericoBiblioteca(Libro.class);
//        DAOGenericoBiblioteca daoPrestamo = new DAOGenericoBiblioteca(Prestamo.class);
//        DAOGenericoBiblioteca daoUsuario = new DAOGenericoBiblioteca(Usuario.class);

//        Usuario usuario = new Usuario("06309765Z","Juan","suertenani@gmail.com","x","normal",null);
//        daoUsuario.addClase(usuario);
//        String jpql = "FROM Usuario u WHERE u.nombre = ?1 AND u.password = ?2";
//        Usuario usuario = (Usuario) daoUsuario.findByQuery(jpql, "juan", "x");
//        System.out.println(usuario);

        Menu menu = new Menu();
        menu.lista();


    }
}
