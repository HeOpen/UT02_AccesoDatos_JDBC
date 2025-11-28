/**
 * Implementación concreta de la interfaz PlayListTrackService.
 * Delega las operaciones en la capa de acceso a datos (PlayListTrackDataAccess).
 * Proporciona una capa de abstracción para la lógica de negocio de las relaciones
 * entre listas de reproducción y tracks.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.services.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlisttrack.PlayListTrackDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class PlayListTrackServiceImpl implements PlayListTrackService {

    // Atributo PlayListTrackDataAccess - inyectado mediante constructor con Lombok
    private final PlayListTrackDataAccess playListTrackDataAccess;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayListTrack> findAllByPlayListId(int playListId) {
        return playListTrackDataAccess.findAllByPlayListId(playListId);
    }
}