package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;

import java.util.List;

public interface PlayListTrackDataAccess {
    List<PlayListTrack> findAllByPlayListId(int playListId);
}
