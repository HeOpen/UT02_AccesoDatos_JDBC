package es.iesclaradelrey.dm2e.ut02.actividad.services.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlisttrack.PlayListTrackDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class PlayListTrackServiceImpl implements PlayListTrackService {
    // Atributo PlayListTrackDataAccess
    private PlayListTrackDataAccess playListTrackDataAccess;

    @Override
    public List<PlayListTrack> findAllByPlayListId(int playListId) {
        return playListTrackDataAccess.findAllByPlayListId(playListId);
    }
}
