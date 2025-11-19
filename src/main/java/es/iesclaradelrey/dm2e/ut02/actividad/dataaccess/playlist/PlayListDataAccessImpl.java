package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import es.iesclaradelrey.dm2e.ut02.actividad.util.ConnectionPool;

import java.sql.*;
import java.util.Optional;

public class PlayListDataAccessImpl implements PlayListDataAccess {
    // Sentencias
    private final String SQL_FIND_BY_ID = "SELECT playlist_id, name FROM playlist WHERE playlist_id = ?";
    private final String SQL_SAVE_EMPTY_PLAYLIST = "INSERT INTO playlist (name) VALUES (?)";
    // fixme: private final String SQL_SAVE_SETTLE_PLAYLIST = "";
    private final String SQL_DELETE_EMPTY_PLAYLIST = "DELETE FROM playlist WHERE playlist_id = ?";
    // fixme: private final String SQL_DELETE_SETTLE_PLAYLIST = "";

    @Override
    public Optional<PlayList> findById(int id) {
        // Intentamos conectarnos a la BBDD
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            // Pasamos el argumento de la función al PreparedStatement para completar la consulta
            preparedStatement.setInt(1, id);

            // Intentamos encontrar resultados
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new PlayList(resultSet.getInt("playlist_id"), resultSet.getString("name")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al encontrar por ID", e);
        }

        // Si no hay coincidencias devuelve un Optional vacío
        return Optional.empty();
    }

    @Override
    public PlayList save(PlayList playList) {
        // Intentamos conectarnos a la BBDD
        try(Connection connection = ConnectionPool.INSTANCE.getConnection()) {

            // Desactivamos el auto-commit
            connection.setAutoCommit(false);

            // Preparamos las consultas
            try (PreparedStatement preparedStatementEmpty = connection.prepareStatement(SQL_SAVE_EMPTY_PLAYLIST, Statement.RETURN_GENERATED_KEYS)) {

                // Pasamos el argumento
                preparedStatementEmpty.setString(1, playList.getPlaylistName());

                // Ejecutamos la sentencia
                preparedStatementEmpty.executeUpdate();

                // Confirmamos la transacción manualmente
                connection.commit();

                // Recuperamos el ID generado automáticamente
                try (ResultSet resultSet = preparedStatementEmpty.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        playList.setPlaylistId(resultSet.getInt("playlist_id"));
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Se ha interrumpido la confirmación de guardado", e);
            } finally {
                // fixme: ¿¿¿Recuperar el auto-commit para el resto de operaciones??? (No creo que sea necesario)
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido realizar la conexión a la BBDD", e);
        }

        // Devolvemos la playlist recogida
        return playList;
    }

    @Override
    public boolean delete(int id) {
        // Intentamos conectar a la BBDD
        try(Connection connection = ConnectionPool.INSTANCE.getConnection();
        PreparedStatement preparedStatementEmpty = connection.prepareStatement(SQL_DELETE_EMPTY_PLAYLIST)) {

            // Pasamos el argumento (ID)
            preparedStatementEmpty.setInt(1, id);

            // Ejecutamos la sentencia y aprovechamos a devolver el bool (si hay columnas modificadas o no)
            return preparedStatementEmpty.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
