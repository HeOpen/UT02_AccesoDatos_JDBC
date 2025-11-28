/**
 * Entidad que representa la relación entre una lista de reproducción y un track.
 * Contiene información de ambas entidades para mostrar datos combinados.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PlayListTrack {
    /**
     * Nombre de la lista de reproducción a la que pertenece el track.
     */
    private String playlistName;

    /**
     * Identificador único de la lista de reproducción.
     */
    private int playlistId;

    /**
     * Identificador único del track en la base de datos.
     */
    private int trackId;

    /**
     * Nombre del track musical.
     */
    private String trackName;
}