package es.iesclaradelrey.dm2e.ut02.actividad.services.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlisttrack.PlayListTrackDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class PlayListTrackServiceImpl implements PlayListTrackService {
    // Atributo PlayListTrackDataAccess
    private final PlayListTrackDataAccess playListTrackDataAccess;

    @Override
    public List<PlayListTrack> findAllByPlayListId(int playListId) {
        return playListTrackDataAccess.findAllByPlayListId(playListId);
    }
}
