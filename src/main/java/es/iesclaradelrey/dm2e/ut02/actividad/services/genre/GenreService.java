/**
 * Interfaz que define los servicios disponibles para la gestión de géneros musicales.
 * Proporciona métodos para operaciones CRUD y consultas sobre la entidad Genre.
 * Actúa como capa de servicio que abstrae la lógica de negocio del acceso a datos.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.services.genre;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    /**
     * Obtiene todos los géneros musicales disponibles en el sistema.
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
    Optional<Genre> findById(Integer id);

    /**
     * Verifica si existe un género musical con el identificador especificado.
     *
     * @param id Identificador del género a verificar
     * @return true si existe un género con ese ID, false en caso contrario
     */
    boolean existsById(int id);

    /**
     * Guarda un nuevo género musical en el sistema.
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
     * Elimina un género musical del sistema por su ID.
     *
     * @param id Identificador del género a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    boolean delete(int id);
}