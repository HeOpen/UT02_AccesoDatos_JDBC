/**
 * Implementación concreta de la interfaz PlayListService.
 * Delega las operaciones en la capa de acceso a datos (PlayListDataAccess).
 * Proporciona una capa de abstracción para la lógica de negocio de listas de reproducción.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.services.playlist;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist.PlayListDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import lombok.AllArgsConstructor;
import java.util.Optional;

@AllArgsConstructor
public class PlayListServiceImpl implements PlayListService {

    // Atributo PlayListDataAccess - inyectado mediante constructor con Lombok
    private final PlayListDataAccess playListDataAccess;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PlayList> findById(int id) {
        return playListDataAccess.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayList save(PlayList playList) {
        return playListDataAccess.save(playList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(int id) {
        return playListDataAccess.delete(id);
    }
}