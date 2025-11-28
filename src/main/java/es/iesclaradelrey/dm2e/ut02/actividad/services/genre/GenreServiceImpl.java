/**
 * Implementación concreta de la interfaz GenreService.
 * Delega las operaciones en la capa de acceso a datos (GenreDataAccess).
 * Proporciona una capa de abstracción para la lógica de negocio de géneros musicales.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.services.genre;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre.GenreDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    // Atributo GenreDataAccess - inyectado mediante constructor con Lombok
    private final GenreDataAccess genreDataAccess;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> findAll() {
        return genreDataAccess.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> findByName(String name) {
        return genreDataAccess.findByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Genre> findById(Integer id) {
        return genreDataAccess.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(int id) {
        return genreDataAccess.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Genre save(Genre genre) {
        return genreDataAccess.save(genre);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Genre update(Genre genre) {
        return genreDataAccess.update(genre);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(int id) {
        return genreDataAccess.delete(id);
    }
}