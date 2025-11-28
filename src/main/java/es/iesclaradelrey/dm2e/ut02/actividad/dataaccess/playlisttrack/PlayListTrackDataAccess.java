/**
 * Interfaz que define las operaciones de acceso a datos para la entidad PlayListTrack.
 * Proporciona métodos para realizar operaciones de consulta sobre las relaciones
 * entre listas de reproducción y tracks.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;

import java.util.List;

public interface PlayListTrackDataAccess {

    /**
     * Busca todos los tracks asociados a una lista de reproducción específica.
     *
     * @param playListId Identificador de la lista de reproducción
     * @return Lista de tracks asociados a la lista de reproducción especificada
     */
    List<PlayListTrack> findAllByPlayListId(int playListId);
}