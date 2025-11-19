package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;

import java.util.Optional;

public interface PlayListDataAccess {
    Optional<PlayList> findById(int id);
    PlayList save(PlayList playList);
    boolean delete(int id);
}
