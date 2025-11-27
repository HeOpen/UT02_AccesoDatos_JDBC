package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.genre;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.Genre;
import es.iesclaradelrey.dm2e.ut02.actividad.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDataAccessImpl implements GenreDataAccess {

    // Sentencias
    private final String SQL_FIND_ALL_GENRES = "SELECT genre_id, name FROM genre";
    private final String SQL_FIND_GENRE_BY_NAME = "SELECT genre_id, name FROM genre WHERE name LIKE ?";
    private final String SQL_FIND_GENRE_BY_ID = "SELECT genre_id, name FROM genre WHERE genre_id = ?";
    private final String SQL_SAVE_GENRE = "INSERT INTO genre (name) VALUES (?)";
    private final String SQL_UPDATE_GENRE = "UPDATE genre SET name = ? WHERE genre_id = ?";
    private final String SQL_DELETE_GENRE = "DELETE FROM genre WHERE genre_id = ?";

    @Override
    public List<Genre> findAll() {
        // Inicializamos la lista general
        List<Genre> genres = new ArrayList<>();

        // Intentamos conectarnos a la BBDD
        // Usamos PreparedStatement por rendimiento (precompilación) y consistencia (en otros métodos puede haber inputs)
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_GENRES);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Iteramos por cada posición con el puntero y añadimos cada una a la lista
            while (resultSet.next()) {
                genres.add(new Genre(resultSet.getInt("genre_id"), resultSet.getString("name")));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de generos", e);
        }

        // Devolvemos la lista
        return genres;
    }

    @Override
    public List<Genre> findByName(String name) {
        // Inicializamos la lista con potenciales coincidencias
        List<Genre> genres = new ArrayList<>();

        // Intentamos conectarnos a la BBDD
        // Volvemos a usar PreparedStatement esta vez por seguridad (evitar inyecciones-SQL)
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_GENRE_BY_NAME)) {

            // Pasamos el argumento de la función al PreparedStatement para completar la consulta
            preparedStatement.setString(1, "%" + name + "%");

            // Intentamos encontrar resultados
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    genres.add(new Genre(resultSet.getInt("genre_id"), resultSet.getString("name")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener coincidencias con '" + name + "'", e);
        }

        return genres;
    }

    @Override
    public Optional<Genre> findById(int id) {
        // Intentamos conectarnos a la BBDD
        // Volvemos a usar PreparedStatement esta vez por seguridad (evitar inyecciones-SQL)
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_GENRE_BY_ID)) {

            // Pasamos el argumento de la función al PreparedStatement para completar la consulta
            preparedStatement.setInt(1, id);

            // Intentamos encontrar resultados
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Si hay una coincidencia (solo puede haber una porque es una PK), la devolvemos
                if (resultSet.next()) {
                    return Optional.of(new Genre(resultSet.getInt("genre_id"), resultSet.getString("name")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al encontrar por ID", e);
        }

        // Si no hay coincidencias, devolvemos el Optional vacío
        return Optional.empty();
    }

    @Override
    public boolean existsById(int id) {
        // Si el método anterior ha encontrado algo, entonces existe (el Optional está presente / no está vacío)
        return findById(id).isPresent();
    }

    @Override
    public Genre save(Genre genre) {
        // Intentamos conectarnos a la BBDD
        // Usamos PreparedStatement con otra sobrecarga para guardar el ID generado para el nuevo género
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_GENRE, Statement.RETURN_GENERATED_KEYS)) {

            // Pasamos el argumento (nombre, el ID lo da autoincrement)
            preparedStatement.setString(1, genre.getName());
            // Ejecutamos la sentencia
            preparedStatement.executeUpdate();
            // Recuperamos el ID y lo guardamos al género
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    genre.setGenreId(resultSet.getInt("genre_id"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el género", e);
        }
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        // Intentamos conectarnos a la BBDD
        try(Connection connection = ConnectionPool.INSTANCE.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_GENRE)) {

            // Pasamos los argumentos
            preparedStatement.setString(1, genre.getName());
            preparedStatement.setInt(2, genre.getGenreId());

            // Ejecutamos la sentencia
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return genre;
    }

    @Override
    public boolean delete(int id) {
        // Intentamos conectarnos a la BBDD
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_GENRE)) {

            // Pasamos el argumento (ID)
            preparedStatement.setInt(1, id);

            // Ejecutamos la sentencia y aprovechamos a devolver el bool (si hay columnas modificadas o no)
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el género", e);
        }
    }
}
