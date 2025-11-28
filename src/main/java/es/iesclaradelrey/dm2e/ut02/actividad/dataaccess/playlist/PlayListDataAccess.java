/**
 * Interfaz que define las operaciones de acceso a datos para la entidad PlayList.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Eliminar)
 * sobre las listas de reproducción en la base de datos.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;

import java.util.Optional;

public interface PlayListDataAccess {

    /**
     * Busca una lista de reproducción por su identificador único.
     *
     * @param id Identificador de la lista de reproducción a buscar
     * @return Optional que contiene la lista de reproducción si existe, 
     *         o vacío si no se encuentra
     */
    Optional<PlayList> findById(int id);

    /**
     * Guarda una nueva lista de reproducción en la base de datos.
     * Incluye la creación de la lista y la asociación de tracks si se proporcionan.
     *
     * @param playList Lista de reproducción a guardar
     * @return La lista de reproducción guardada con su ID asignado
     */
    PlayList save(PlayList playList);

    /**
     * Elimina una lista de reproducción de la base de datos por su ID.
     * La eliminación incluye tanto la lista como las relaciones con tracks asociados.
     *
     * @param id Identificador de la lista de reproducción a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    boolean delete(int id);
}
