package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import es.iesclaradelrey.dm2e.ut02.actividad.util.ConnectionPool;

import java.sql.*;
import java.util.Optional;

public class PlayListDataAccessImpl implements PlayListDataAccess {
    // Sentencias
    private final String SQL_FIND_BY_ID = "SELECT playlist_id, name FROM playlist WHERE playlist_id = ?";
    private final String SQL_SAVE_INTO_TABLE_PLAYLIST = "INSERT INTO playlist (name) VALUES (?)";
    private final String SQL_SAVE_INTO_TABLE_PLAYLIST_TRACK = "INSERT INTO playlist_track (playlist_id, track_id) VALUES (?, ?)";
    private final String SQL_DELETE_FROM_TABLE_PLAYLIST = "DELETE FROM playlist WHERE playlist_id = ?";
    private final String SQL_DELETE_FROM_TABLE_PLAYLIST_TRACK = "DELETE FROM playlist_track WHERE playlist_id = ?";

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
                    return Optional.of(PlayList.builder().playlistId(resultSet.getInt("playlist_id")).playlistName(resultSet.getString("name")).build());
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
        try (Connection connection = ConnectionPool.INSTANCE.getConnection()) {

            // Desactivamos el auto-commit
            connection.setAutoCommit(false);

            // Preparamos las consultas
            try (PreparedStatement preparedStatementPlaylist = connection.prepareStatement(SQL_SAVE_INTO_TABLE_PLAYLIST, Statement.RETURN_GENERATED_KEYS)) {

                // ----- Primero: insertamos la playlist nueva en la tabla 'playlist' ----- //
                // Pasamos el argumento
                preparedStatementPlaylist.setString(1, playList.getPlaylistName());

                // Ejecutamos la sentencia
                preparedStatementPlaylist.executeUpdate();

                // Recuperamos el ID generado automáticamente
                try (ResultSet resultSet = preparedStatementPlaylist.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        playList.setPlaylistId(resultSet.getInt("playlist_id"));
                    }
                }

                // ----- Segundo: con el ID generado, insertamos la relacion entre la playlist y sus tracks ----- //
                // Por cada track en su lista, hacemos un insert del ID del track
                try {

                    for (PlayListTrack track : playList.getTracks()) {

                        try (PreparedStatement preparedStatementPlaylistTrack = connection.prepareStatement(SQL_SAVE_INTO_TABLE_PLAYLIST_TRACK)) {

                            // Pasamos los argumentos
                            preparedStatementPlaylistTrack.setInt(1, playList.getPlaylistId());
                            preparedStatementPlaylistTrack.setInt(2, track.getTrackId());

                            // Ejecutamos la sentencia
                            preparedStatementPlaylistTrack.executeUpdate();

                        } catch (SQLException e) {
                            throw new RuntimeException("Error al guardar una track de la playlist", e);
                        }
                    }

                    // Finalmente confirmamos la transacción manualmente
                    connection.commit();

                } catch (SQLException e) {
                    connection.rollback();
                    throw new RuntimeException("Error al intentar guardar cada track ID con su playlist ID", e);
                }

            } catch (SQLException e) {
                throw new RuntimeException("Falló la consulta", e);
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
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatementPlaylist = connection.prepareStatement(SQL_DELETE_FROM_TABLE_PLAYLIST);
             PreparedStatement preparedStatementTracks = connection.prepareStatement(SQL_DELETE_FROM_TABLE_PLAYLIST_TRACK)) {

            // Pasamos el argumento (ID)
            preparedStatementPlaylist.setInt(1, id);
            preparedStatementTracks.setInt(1, id);

            // Ejecutamos la sentencia y aprovechamos a devolver el bool (si hay columnas modificadas o no)
            // EL ORDEN IMPORTA !!!
            int registrosModficiadosTrack = preparedStatementTracks.executeUpdate();
            int registrosModficiadosPlaylist = preparedStatementPlaylist.executeUpdate();
            return registrosModficiadosPlaylist + registrosModficiadosTrack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido realizar la conexión a la BBDD", e);
        }
    }
}
