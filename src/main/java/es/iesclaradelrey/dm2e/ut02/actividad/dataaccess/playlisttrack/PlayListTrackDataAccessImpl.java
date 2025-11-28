package es.iesclaradelrey.dm2e.ut02.actividad.dataaccess.playlisttrack;

import es.iesclaradelrey.dm2e.ut02.actividad.entities.PlayListTrack;
import es.iesclaradelrey.dm2e.ut02.actividad.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayListTrackDataAccessImpl implements PlayListTrackDataAccess {

    // Sentencias
    private final String SQL_FIND_ALL_BY_PLAYLIST_ID = """
            SELECT p.name as playlist_name, pt.playlist_id, pt.track_id, t.name as track_name
            FROM playlist_track pt
            INNER JOIN track t on t.track_id = pt.track_id
            INNER JOIN playlist p on p.playlist_id = pt.playlist_id
            WHERE pt.playlist_id = ?;
            """;

    @Override
    public List<PlayListTrack> findAllByPlayListId(int playListId) {
        // Inicializamos una lista vacia
        List<PlayListTrack> playListTracks = new ArrayList<>();

        // Intentamos conectarnos a la BBDD
        try(Connection connection = ConnectionPool.INSTANCE.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_BY_PLAYLIST_ID)) {

            // Le pasamos el argumento ID
            preparedStatement.setInt(1, playListId);

            // Intentamos recuperar los datos obtenidos
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    playListTracks.add(
                            PlayListTrack.builder()
                                    .playlistName(resultSet.getString("playlist_name"))
                                    .playlistId(resultSet.getInt("playlist_id"))
                                    .trackId(resultSet.getInt("track_id"))
                                    .trackName(resultSet.getString("track_name"))
                                    .build());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se ha podido realizar la conexión a la BBDD", e);
        }


        return playListTracks;
    }
}
