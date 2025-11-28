package es.iesclaradelrey.dm2e.ut02.actividad.services.genre;

import es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre.GenreDataAccess;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    // Atributo GenreDataAccess
    private final GenreDataAccess genreDataAccess;

    @Override
    public List<Genre> findAll() {
        return genreDataAccess.findAll();
    }

    @Override
    public List<Genre> findByName(String name) {
        return genreDataAccess.findByName(name);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        return genreDataAccess.findById(id);
    }

    @Override
    public boolean existsById(int id) {
        return genreDataAccess.existsById(id);
    }

    @Override
    public Genre save(Genre genre) {
        return genreDataAccess.save(genre);
    }

    @Override
    public Genre update(Genre genre) {
        return genreDataAccess.update(genre);
    }

    @Override
    public boolean delete(int id) {
        return genreDataAccess.delete(id);
    }
}
