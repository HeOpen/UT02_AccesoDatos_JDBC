package es.iesclaradelrey.dm2e.ut02.actividad.actividad;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre.GenreDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist.PlayListDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlisttrack.PlayListTrackDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import es.iesclaradelrey.dm2e.ut02.actividad.services.genre.GenreService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.genre.GenreServiceImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlist.PlayListService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlist.PlayListServiceImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlisttrack.PlayListTrackService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlisttrack.PlayListTrackServiceImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.util.ConnectionPool;

import java.util.*;
import java.util.logging.Logger;

public class Main {

    // Declaración de servicios
    private final static GenreService GENRE_SERVICE = new GenreServiceImpl(new GenreDataAccessImpl());
    private final static PlayListService PLAYLIST_SERVICE = new PlayListServiceImpl(new PlayListDataAccessImpl());
    private final static PlayListTrackService PLAYLISTTRACK_SERVICE = new PlayListTrackServiceImpl(new PlayListTrackDataAccessImpl());

    // Scanner
    private final static Scanner SCANNER = new Scanner(System.in);

    // Tiempo para enseñar el error si es que se lanza alguna excepción
    private final static Long TIME_TO_SHOW = 3000L;

    // Un logger (por probarlo)
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

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
        String inputRecogida = SCANNER.nextLine();

        try {
            int id = Integer.parseInt(inputRecogida);
            Optional<Genre> genero = GENRE_SERVICE.findById(id);

            if (genero.isPresent()) {
                System.out.printf("El género con id <%s> es <%s>\n", genero.get().getGenreId(), genero.get().getName());
            } else {
                System.out.printf("No se encuentra el género con id <%d>\n", id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número entero.");
        }
    }

    private static void buscarGeneroPorNombre() {
        System.out.println("Introduce el nombre del género a buscar:");
        String nombre = SCANNER.nextLine().trim();

        // Comprobamos que el nombre no está vacio
        if (!nombre.isEmpty()) {

            List<Genre> posiblesGeneros = GENRE_SERVICE.findByName(nombre);

            if (!posiblesGeneros.isEmpty()) {
                posiblesGeneros.forEach(genre -> System.out.printf("%d - %s\n", genre.getGenreId(), genre.getName()));
            } else {
                System.out.printf("No se encuentran géneros que contengan <%s> en el nombre\n", nombre);
            }

        } else {
            System.out.println("Debes introducir un nombre para buscar.");
        }
    }

    private static void crearNuevoGenero() {
        System.out.println("Introduce el nombre del nuevo género a guardar");
        String nombre = SCANNER.nextLine().trim();

        // Comprobamos que el nombre no está vacio
        if (!nombre.isEmpty()) {

            try {
                Genre nuevo = GENRE_SERVICE.save(new Genre(nombre));
                System.out.printf("Se ha creado el género <%s> con id <%d>", nuevo.getName(), nuevo.getGenreId());
            } catch (RuntimeException e) {
                // Recogemos el throw new RuntimeException("Error al guardar el género", e); de GenreDataAccessImpl
                System.out.printf("Error en la operación <%s>\n", e.getMessage());
            }

        } else {
            System.out.println("Debes introducir un nombre de género para guardar.");
        }
    }

    private static void modificarGeneroExistente() {
        System.out.println("Introduce el ID del género a modificar:");
        String inputRecogida = SCANNER.nextLine().trim();

        try {
            int id = Integer.parseInt(inputRecogida);

            // Instanciamos un 'optional' para ver qué nos devuelve
            Optional<Genre> genre = GENRE_SERVICE.findById(id);

            if (genre.isPresent()) {
                // Guardamos el anterior nombre
                String nombreAntiguo = genre.get().getName();

                // Guardamos el nuevo nombre
                System.out.println("¿Que nombre debería tener este género?:");
                String nombreNuevo = SCANNER.nextLine().trim();

                // Comprobamos que el nuevo nombre no está vacio
                if (nombreNuevo.isEmpty()) {
                    System.out.println("Debes introducir un nombre, no puede estar vacío");
                    return;
                }

                // Comprobamos que no es el mismo nombre
                if (nombreNuevo.equals(nombreAntiguo)) {
                    System.out.println("Intentas guardar el género con el mismo nombre que ya tenía.");
                    return;
                }

                // Ejecutamos la sentencia
                try {
                    GENRE_SERVICE.update(new Genre(id, nombreNuevo));
                    System.out.printf("Se ha modificado el género <%s>. Su nuevo nombre es <%s>\n", nombreAntiguo, nombreNuevo);
                } catch (RuntimeException e) {
                    System.out.printf("Error en la operación <%s>\n", e.getMessage());
                }

            } else {
                System.out.printf("No existe el género con id <%d>\n", id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número entero.");
        }

    }

    private static void eliminarGeneroPorID() {
        System.out.println("Introduce el ID del género a eliminar:");
        String inputRecogida = SCANNER.nextLine().trim();

        try {
            int id = Integer.parseInt(inputRecogida);

            // Instanciamos un 'optional' para ver qué nos devuelve
            Optional<Genre> genre = GENRE_SERVICE.findById(id);

            if (genre.isPresent()) {
                // Ejecutamos la sentencia
                try {
                    GENRE_SERVICE.delete(id);
                    System.out.printf("Se ha eliminado el género con id <%s>\n", genre.get().getGenreId());
                } catch (RuntimeException e) {
                    System.out.printf("Error en la operación <%s>\n", e.getMessage());
                }

            } else {
                System.out.printf("No existe el género con id <%d>\n", id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número entero.");
        }

    }

    // ----- Métodos para las opciones de menu (con la tabla PLAYLIST) ----- //
    private static void crearListaDeReproduccion() {
        // Recogemos el nombre de la nueva lista
        System.out.println("Introduce el nombre de la nueva lista de reproduccion:");
        String nombre = SCANNER.nextLine().trim();

        // Comprobamos que el nuevo nombre no está vacio
        if (nombre.isEmpty()) {
            System.out.println("Debes introducir un nombre, no puede estar vacío");
            return;
        }

        // Recogemos los ids de los tracks
        System.out.println("Introduce los id de los tracks (separados por coma, estilo csv):");
        try {
            List<Integer> listadoInts = Arrays.stream(SCANNER.nextLine().split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();

            if (listadoInts.isEmpty()) {
                System.out.println("Debes introducir al menos un track ID.");
                return;
            }

            // Generamos los PlayListTrack
            List<PlayListTrack> listadoTracks = new ArrayList<>();

            listadoInts.forEach(i -> {
                listadoTracks.add(PlayListTrack.builder().trackId(i).build());
            });

            // Llamamos al servicio
            try {
                PLAYLIST_SERVICE.save(PlayList.builder().playlistName(nombre).tracks(listadoTracks).build());
            } catch (RuntimeException e) {
                System.out.printf("Error en la operación <%s>\n", e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: todos los valores deben ser números enteros separados por coma, estilo csv");
        }
    }

    private static void buscarListaDeReproduccionPorID() {
        // Recogemos el ID de la lista de reproducción
        System.out.println("Introduce el id de la lista de reproduccion a mostrar:");
        String inputRecogida = SCANNER.nextLine().trim();

        try {
            int id = Integer.parseInt(inputRecogida);

            // Comprobamos que existe la lista de reproduccion
            try {
                Optional<PlayList> playlist = PLAYLIST_SERVICE.findById(id);

                if (playlist.isPresent()) {
                    // Si existe buscamos las tracks asociadas
                    try {

                        List<PlayListTrack> playListTracks = PLAYLISTTRACK_SERVICE.findAllByPlayListId(id);

                        if (playListTracks.isEmpty()) {
                            System.out.println("No existen tracks asociadas en la lista de reproducción");
                        } else {
                            // Imprimimos la cabecera
                            System.out.printf("Lista de reproducción: %s\n", playlist.get().getPlaylistName());
                            // Imprimimos cada pista de la lista
                            playListTracks.forEach(track -> {
                                System.out.printf("%d - %s -> %d - %s\n", track.getPlaylistId(), track.getPlaylistName(), track.getTrackId(), track.getTrackName());
                            });
                        }

                    } catch (RuntimeException e) {
                        System.out.printf("Error en la operación <%s>\n", e.getMessage());
                    }

                } else {
                    System.out.printf("No existe una lista de reproducción con id <%d>\n", id);
                }

            } catch (RuntimeException e) {
                System.out.printf("Error en la operación <%s>\n", e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número entero.");
        }
    }

    private static void eliminarListaDeReproduccionPorID() {
        // Recogemos el ID de la lista de reproducción
        System.out.println("Introduce el id de la lista de reproduccion a eliminar:");
        String inputRecogida = SCANNER.nextLine().trim();

        try {
            int id = Integer.parseInt(inputRecogida);

            // Instanciamos un 'optional' para ver si existe
            try {

                Optional<PlayList> lista = PLAYLIST_SERVICE.findById(id);

                // Si devuelve diferente de 'isEmpty' la eliminamos
                if (lista.isPresent()) {
                    PLAYLIST_SERVICE.delete(id);
                    System.out.printf("Se ha eliminado la lista con id <%d>\n", id);
                } else {
                    System.out.printf("No existe la lista de reproducción con id <%d>\n>", id);
                }

            } catch (RuntimeException e) {
                System.out.printf("Error en la operación <%s>\n", e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un numero entero.");
        }
    }

    private static void ejecutarOpcionSeleccionada(int opcion) {
        switch (opcion) {
            case 0 -> {
                System.out.printf("Opción seleccionada: %d (SALIENDO DEL PROGRAMA...)\n", opcion);
                // Fin de programa
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.out.println("Cerrando pool de conexiones...");
                    ConnectionPool.INSTANCE.closePool();
                }));
                System.exit(0);
            }
            case 1 -> {
                System.out.printf("Opción seleccionada: %d (buscar TODOS los géneros)\n", opcion);
                buscarTodosGeneros();
                volverMainMenu();
            }
            case 2 -> {
                System.out.printf("Opción seleccionada: %d (buscar género por ID)\n", opcion);
                buscarGeneroPorID();
                volverMainMenu();
            }
            case 3 -> {
                System.out.printf("Opción seleccionada: %d (buscar género por NOMBRE)\n", opcion);
                buscarGeneroPorNombre();
                volverMainMenu();
            }
            case 4 -> {
                System.out.printf("Opción seleccionada: %d (crear NUEVO género)\n", opcion);
                crearNuevoGenero();
                volverMainMenu();
            }
            case 5 -> {
                System.out.printf("Opción seleccionada: %d (MODIFICAR género por ID)\n", opcion);
                modificarGeneroExistente();
                volverMainMenu();
            }
            case 6 -> {
                System.out.printf("Opción seleccionada: %d (ELIMINAR género por ID)\n", opcion);
                eliminarGeneroPorID();
                volverMainMenu();
            }
            case 7 -> {
                System.out.printf("Opción seleccionada: %d (crear NUEVA lista de reproducción)\n", opcion);
                crearListaDeReproduccion();
                volverMainMenu();
            }
            case 8 -> {
                System.out.printf("Opción seleccionada: %d (buscar lista por ID)\n", opcion);
                buscarListaDeReproduccionPorID();
                volverMainMenu();
            }
            case 9 -> {
                System.out.printf("Opción seleccionada: %d (eliminar lista por ID)\n", opcion);
                eliminarListaDeReproduccionPorID();
                volverMainMenu();
            }
        }
    }

    private static void seleccionarNuevaOpcion() {
        int opcion = inputOpcion();
        ejecutarOpcionSeleccionada(opcion);
    }

    private static void volverMainMenu() {
        try {
            System.out.println("Volviendo a la pantalla principal");
            Thread.sleep(TIME_TO_SHOW);
        } catch (InterruptedException ex) {
            LOGGER.info(ex.getMessage());
        }
        seleccionarNuevaOpcion();
    }

    public static void main(String[] args) {
        seleccionarNuevaOpcion();
    }
}