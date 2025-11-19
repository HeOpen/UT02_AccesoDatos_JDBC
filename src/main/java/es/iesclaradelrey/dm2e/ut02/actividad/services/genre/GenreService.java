package es.iesclaradelrey.dm2e.ut02.actividad.services.genre;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> findAll();
    List<Genre> findByName(String name);
    Optional<Genre> findById(Integer id);
    boolean existsById(int id);
    Genre save(Genre genre);
    boolean delete(int id);
}
