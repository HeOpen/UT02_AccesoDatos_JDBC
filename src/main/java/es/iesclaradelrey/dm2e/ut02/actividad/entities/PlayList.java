package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class PlayList {
    // Atributos
    private int playlistId;
    private String playlistName;
    private List<PlayListTrack> tracks;
}
