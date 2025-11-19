package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlayList {
    // Atributos
    private int playlistId;
    private String playlistName;
    private List<PlayListTrack> tracks;

    // Constructor específico sin la lista de tracks
    public PlayList(int playlistId, String playlistName) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }
}
