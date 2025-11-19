package es.iesclaradelrey.dm2e.ut02.actividad.services.playlist;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist.PlayListDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;

import java.util.Optional;

public class PlayListServiceImpl implements PlayListService {

    // Atributo PlayListDataAccess
    private PlayListDataAccess playListDataAccess;

    @Override
    public Optional<PlayList> findById(int id) {
        return playListDataAccess.findById(id);
    }

    @Override
    public PlayList save(PlayList playList) {
        return playListDataAccess.save(playList);
    }

    @Override
    public boolean delete(int id) {
        return playListDataAccess.delete(id);
    }
}
