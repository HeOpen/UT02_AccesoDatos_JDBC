/**
 * Entidad que representa una lista de reproducción en el sistema.
 * Contiene la información de la lista y los tracks asociados a ella.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@ToString
public class PlayList {
    /**
     * Identificador único de la lista de reproducción en la base de datos.
     * Valor generado automáticamente por la base de datos.
     */
    private int playlistId;

    /**
     * Nombre de la lista de reproducción.
     * Ejemplos: "Mis favoritas", "Música para estudiar", "Rock clásico"
     */
    private String playlistName;

    /**
     * Lista de tracks asociados a esta lista de reproducción.
     * Puede estar vacía si la lista no tiene tracks asociados.
     */
    private List<PlayListTrack> tracks;
}