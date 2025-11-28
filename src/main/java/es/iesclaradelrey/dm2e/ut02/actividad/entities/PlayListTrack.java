package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PlayListTrack {
    private String playlistName;
    private int playlistId;
    private int trackId;
    private String trackName;
}
