package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

import java.util.List;

@Builder
@Getter
@ToString
public class PlayList {
    // Atributos
    private int playlistId;
    private String playlistName;
    private List<PlayListTrack> tracks;
}
