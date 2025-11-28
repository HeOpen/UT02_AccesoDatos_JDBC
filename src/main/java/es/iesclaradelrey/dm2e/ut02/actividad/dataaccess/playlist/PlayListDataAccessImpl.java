/**
 * Implementación concreta de la interfaz PlayListDataAccess.
 * Proporciona la lógica de acceso a datos para la entidad PlayList utilizando JDBC.
 * Maneja operaciones transaccionales para garantizar la consistencia de datos
 * al crear y eliminar listas de reproducción con sus tracks asociados.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlist;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayList;
import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import es.iesclaradelrey.dm2e.ut02.actividad.utils.ConnectionPool;

import java.sql.*;
import java.util.Optional;

public class PlayListDataAccessImpl implements PlayListDataAccess {

    // Sentencias SQL predefinidas para mejorar legibilidad y mantenibilidad
    private final String SQL_FIND_BY_ID = "SELECT playlist_id, name FROM playlist WHERE playlist_id = ?";
    private final String SQL_SAVE_INTO_TABLE_PLAYLIST = "INSERT INTO playlist (name) VALUES (?)";
    private final String SQL_SAVE_INTO_TABLE_PLAYLIST_TRACK = "INSERT INTO playlist_track (playlist_id, track_id) VALUES (?, ?)";
    private final String SQL_DELETE_FROM_TABLE_PLAYLIST = "DELETE FROM playlist WHERE playlist_id = ?";
    private final String SQL_DELETE_FROM_TABLE_PLAYLIST_TRACK = "DELETE FROM playlist_track WHERE playlist_id = ?";

    /**
     * {@inheritDoc}
     */
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
                    return Optional.of(PlayList.builder()
                            .playlistId(resultSet.getInt("playlist_id"))
                            .playlistName(resultSet.getString("name"))
                            .build());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al encontrar por ID", e);
        }

        // Si no hay coincidencias devuelve un Optional vacío
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * Implementa una operación transaccional que:
     * 1. Inserta la nueva lista de reproducción en la tabla 'playlist'
     * 2. Inserta todas las relaciones con tracks en la tabla 'playlist_track'
     * 3. Realiza rollback en caso de error para mantener la consistencia de datos
     */
    @Override
    public PlayList save(PlayList playList) {
        // Intentamos conectarnos a la BBDD
        try (Connection connection = ConnectionPool.INSTANCE.getConnection()) {

            // Desactivamos el auto-commit para manejar la transacción manualmente
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

                // ----- Segundo: con el ID generado, insertamos la relación entre la playlist y sus tracks ----- //
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
                    // Si hay error, revertimos todos los cambios
                    connection.rollback();
                    throw new RuntimeException("Error al intentar guardar cada track ID con su playlist ID", e);
                }

            } catch (SQLException e) {
                throw new RuntimeException("Falló la consulta", e);
            } finally {
                // Restauramos el auto-commit antes de devolver la conexión al pool
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido realizar la conexión a la BBDD", e);
        }

        // Devolvemos la playlist recogida
        return playList;
    }

    /**
     * {@inheritDoc}
     *
     * Implementa una operación que elimina tanto la lista de reproducción
     * como todas sus relaciones con tracks. El orden de eliminación es importante:
     * primero se eliminan las relaciones en 'playlist_track' y luego la lista en 'playlist'
     * para respetar las restricciones de integridad referencial.
     */
    @Override
    public boolean delete(int id) {
        // Intentamos conectar a la BBDD
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement preparedStatementPlaylist = connection.prepareStatement(SQL_DELETE_FROM_TABLE_PLAYLIST);
             PreparedStatement preparedStatementTracks = connection.prepareStatement(SQL_DELETE_FROM_TABLE_PLAYLIST_TRACK)) {

            // Pasamos el argumento (ID) a ambos prepared statements
            preparedStatementPlaylist.setInt(1, id);
            preparedStatementTracks.setInt(1, id);

            // Ejecutamos las sentencias y aprovechamos a devolver el bool (si hay columnas modificadas o no)
            // EL ORDEN IMPORTA: primero eliminamos las relaciones, luego la lista principal
            int registrosModificadosTrack = preparedStatementTracks.executeUpdate();
            int registrosModificadosPlaylist = preparedStatementPlaylist.executeUpdate();

            // Consideramos la operación exitosa si se modificó al menos un registro
            return registrosModificadosPlaylist + registrosModificadosTrack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido realizar la conexión a la BBDD", e);
        }
    }
}