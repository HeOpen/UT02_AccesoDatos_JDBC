/**
 * Interfaz que define las operaciones de acceso a datos para la entidad Genre.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre los géneros musicales en la base de datos.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDataAccess {

    /**
     * Obtiene todos los géneros musicales existentes en la base de datos.
     *
     * @return Lista de todos los géneros musicales
     */
    List<Genre> findAll();

    /**
     * Busca géneros musicales cuyo nombre contenga el texto especificado.
     *
     * @param name Texto a buscar en los nombres de los géneros
     * @return Lista de géneros que coinciden con el criterio de búsqueda
     */
    List<Genre> findByName(String name);

    /**
     * Busca un género musical por su identificador único.
     *
     * @param id Identificador del género a buscar
     * @return Optional que contiene el género si existe, o vacío si no se encuentra
     */
    Optional<Genre> findById(int id);

    /**
     * Verifica si existe un género musical con el identificador especificado.
     *
     * @param id Identificador del género a verificar
     * @return true si existe un género con ese ID, false en caso contrario
     */
    boolean existsById(int id);

    /**
     * Guarda un nuevo género musical en la base de datos.
     *
     * @param genre Género a guardar
     * @return El género guardado con su ID asignado
     */
    Genre save(Genre genre);

    /**
     * Actualiza la información de un género musical existente.
     *
     * @param genre Género con la información actualizada
     * @return El género actualizado
     */
    Genre update(Genre genre);

    /**
     * Elimina un género musical de la base de datos por su ID.
     *
     * @param id Identificador del género a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    boolean delete(int id);
}