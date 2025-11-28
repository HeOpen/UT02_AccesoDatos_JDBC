/**
 * Interfaz que define los servicios disponibles para la gestión de relaciones
 * entre listas de reproducción y tracks.
 * Proporciona métodos para consultar las tracks asociadas a listas de reproducción.
 * Actúa como capa de servicio que abstrae la lógica de negocio del acceso a datos.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.services.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;

import java.util.List;

public interface PlayListTrackService {

    /**
     * Busca todos los tracks asociados a una lista de reproducción específica.
     * Incluye información completa de cada track y de la lista de reproducción.
     *
     * @param playListId Identificador de la lista de reproducción
     * @return Lista de tracks asociados a la lista de reproducción especificada,
     *         con información completa de cada track y la playlist
     */
    List<PlayListTrack> findAllByPlayListId(int playListId);
}