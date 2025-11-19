package es.iesclaradelrey.dm2e.ut02.actividad.actividad;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre.GenreDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre.GenreDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist.PlayListDataAccessImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;
import es.iesclaradelrey.dm2e.ut02.actividad.services.genre.GenreService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.genre.GenreServiceImpl;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlist.PlayListService;
import es.iesclaradelrey.dm2e.ut02.actividad.services.playlist.PlayListServiceImpl;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

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

    private final static GenreService GENRE_SERVICE = new GenreServiceImpl(new GenreDataAccessImpl());
    private final static PlayListService PLAYLIST_SERVICE = new PlayListServiceImpl(new PlayListDataAccessImpl());

    public static void main(String[] args) {
        int opcion = inputOpcion();
        System.out.printf("Opción seleccionada: %d", opcion);

        // Redirigir a la opción deseada
        switch (opcion) {
            case 0 -> System.exit(0);

            case 1 -> GENRE_SERVICE.findAll();

            case 2 -> {
                System.out.println("Introduce el ID del género");
                // fixme: ¿¿¿Y si no introduce un número???
                // fixme: quizas separar cada función mejor y hacer el switch-case con funciones
                int id = SCANNER.nextInt();
                GENRE_SERVICE.findById(id);
            }

            case 3 -> {
                System.out.println("Introduce el nombre del género");
                String nombre = SCANNER.nextLine().trim();
                GENRE_SERVICE.findByName(nombre);
            }

            case 4 -> {
                System.out.println("Introduce el nombre del nuevo género a guardar");
                String nombre = SCANNER.nextLine().trim();
                GENRE_SERVICE.save(new Genre(nombre));
            }

            // fixme: ETC para los 9 casos
        }
    }
}