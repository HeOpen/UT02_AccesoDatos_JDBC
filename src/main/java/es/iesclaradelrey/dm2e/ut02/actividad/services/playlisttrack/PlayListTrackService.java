package es.iesclaradelrey.dm2e.ut02.actividad.services.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;

import java.util.List;

public interface PlayListTrackService {
    List<PlayListTrack> findAllByPlayListId(int playListId);
}
