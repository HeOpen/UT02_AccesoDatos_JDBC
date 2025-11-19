package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class PlayListTrack {
    private int id;
    private String name;
    private int albumId;
    private int mediaTypeId;
    private int genreId;
    private String composer;
    private long milliseconds;
    private long bytes;
    private float unitPrice;
}
