package es.iesclaradelrey.dm2e.ut02.actividad.actividad;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre.GenreDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist.PlayListDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import es.iesclaradelrey.dm2e.ut02.actividad.services.genre.GenreService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.genre.GenreServiceImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlist.PlayListService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlist.PlayListServiceImpl;

import java.util.*;

public class Main {

    // Declaración de servicios
    private final static GenreService GENRE_SERVICE = new GenreServiceImpl(new GenreDataAccessImpl());
    private final static PlayListService PLAYLIST_SERVICE = new PlayListServiceImpl(new PlayListDataAccessImpl());

    // Scanner
    private final static Scanner SCANNER = new Scanner(System.in);

    // Interfaz / menu principal
    private final static String MAIN_MENU_TEXT =
            """
                    GESTIÓN DE GÉNEROS Y LISTAS DE REPRODUCCIÓN
                    -------------------------------------------------------
                    1. Buscar todos los géneros
                    2. Buscar género por ID
                    3. Buscar géneros por nombre
                    4. Crear un nuevo género
                    5. Modificar un género existente
                    6. Eliminar un género por ID
                    7. Crear nueva lista de reproducción
                    8. Buscar lista de reproducción por ID
                    9. Eliminar lista de reproducción por ID
                    0. Salir
                    Seleccione una opción:
                    """;

    // Método para recoger un número por consola
    private static int inputOpcion() {

        // Bucle para pedir número en caso de fallar por formato
        while (true) {

            System.out.print(MAIN_MENU_TEXT);
            String input = SCANNER.nextLine().trim();

            try {
                // Comprobamos que es un número, intentado parsearlo
                int opcion = Integer.parseInt(input);

                // Comprobamos que está en rango
                if (opcion < 0 || opcion > 9) {
                    System.out.println("Número fuera de rango (0-9). Intenta de nuevo:");
                } else {
                    return opcion;
                }

            } catch (NumberFormatException e) {
                System.out.println("Formato incorrecto, introduce un número:");
            }
        }
    }


    // ----- Métodos para las opciones de menu (con la tabla GENEROS) ----- //
    private static void buscarTodosGeneros() {
        GENRE_SERVICE.findAll().forEach(genre -> System.out.printf("%d - %s\n", genre.getGenreId(), genre.getName()));
    }

    private static void buscarGeneroPorID() {
        System.out.println("Introduce el ID del género a buscar:");
        // fixme: ¿¿¿Y si no introduce un número???
        int id = SCANNER.nextInt();
        Optional<Genre> genero = GENRE_SERVICE.findById(id);

        // fixme: debería ir en la capa de servicios los checks???
        if (genero.isPresent()) {
            System.out.printf("El género con id <%s> es <%s>\n", genero.get().getGenreId(), genero.get().getName());
        } else {
            // fixme: throw new RuntimeException(); ??? No lo hace ya el método en dataaccess ???
        }
    }

    private static void buscarGeneroPorNombre() {
        System.out.println("Introduce el nombre del género a buscar:");
        String nombre = SCANNER.nextLine().trim();
        List<Genre> posiblesGeneros = GENRE_SERVICE.findByName(nombre);

        if (!posiblesGeneros.isEmpty()) {
            posiblesGeneros.forEach(genre -> System.out.printf("%d - %s\n", genre.getGenreId(), genre.getName()));
        } else {
            System.out.printf("No se encuentran géneros que contengan <%s> en el nombre\n", nombre);
        }
    }

    private static void crearNuevoGenero() {
        System.out.println("Introduce el nombre del nuevo género a guardar");
        String nombre = SCANNER.nextLine().trim();
        GENRE_SERVICE.save(new Genre(nombre));
    }

    private static void modificarGeneroExistente() {
        System.out.println("Introduce el ID del género a modificar:");
        int id = Integer.parseInt(SCANNER.nextLine().trim());

        // Instanciamos un 'optional' para ver qué nos devuelve
        Optional<Genre> genre = GENRE_SERVICE.findById(id);

        if (genre.isPresent()) {
            // Guardamos el anterior nombre
            String nombreAntiguo = genre.get().getName();

            // Guardamos el nuevo nombre
            System.out.println("¿Que nombre debería tener este género?:");
            String nombreNuevo = SCANNER.nextLine().trim();

            // Ejecutamos la sentencia
            GENRE_SERVICE.update(new Genre(id, nombreNuevo));
            System.out.printf("Se ha modificado el género <%s>. Su nuevo nombre es <%s>\n", nombreAntiguo, nombreNuevo);

        } else {
            System.out.printf("No existe el género con id <%d>\n", id);
        }

    }

    private static void eliminarGeneroPorID() {
        System.out.println("Introduce el ID del género a eliminar:");
        int id = Integer.parseInt(SCANNER.nextLine().trim());

        // Instanciamos un 'optional' para ver qué nos devuelve
        Optional<Genre> genre = GENRE_SERVICE.findById(id);

        if (genre.isPresent()) {
            // Ejecutamos la sentencia
            GENRE_SERVICE.delete(id);
            System.out.printf("Se ha eliminado el género con id <%s>\n", genre.get().getGenreId());

        } else {
            System.out.printf("No existe el género con id <%d>\n", id);
        }

    }

    // ----- Métodos para las opciones de menu (con la tabla PLAYLIST) ----- //
    private static void crearListaDeReproduccion() {
        // Recogemos el nombre de la nueva lista
        System.out.println("Introduce el nombre de la nueva lista de reproduccion:");
        String nombre = SCANNER.nextLine().trim();

        // Recogemos los ids de los tracks
        System.out.println("Introduce los id de los tracks (separados por coma, estilo csv):");
        List<Integer> listadoInts = Arrays.stream(SCANNER.nextLine().split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        // Generamos los PlayListTrack
        List<PlayListTrack> listadoTracks = new ArrayList<>();

        listadoInts.forEach(i -> {
            listadoTracks.add(PlayListTrack.builder().trackId(i).build());
        });

        // Llamamos al servicio
        PLAYLIST_SERVICE.save(PlayList.builder().playlistName(nombre).tracks(listadoTracks).build());
    }

    private static void buscarListaDeReproduccionPorID() {
    }

    private static void eliminarListaDeReproduccionPorID() {
    }

    private static void ejecutarOpcionSeleccionada(int opcion) {
        switch (opcion) {
            case 0 -> System.exit(0);
            case 1 -> buscarTodosGeneros();
            case 2 -> buscarGeneroPorID();
            case 3 -> buscarGeneroPorNombre();
            case 4 -> crearNuevoGenero();
            case 5 -> modificarGeneroExistente();
            case 6 -> eliminarGeneroPorID();
            case 7 -> crearListaDeReproduccion();
            case 8 -> buscarListaDeReproduccionPorID();
            case 9 -> eliminarListaDeReproduccionPorID();
        }
    }

    public static void main(String[] args) {
        int opcion = inputOpcion();
        System.out.printf("Opción seleccionada: %d\n", opcion);
        ejecutarOpcionSeleccionada(opcion);
    }
}