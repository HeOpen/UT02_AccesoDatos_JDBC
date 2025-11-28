/**
 * Entidad que representa un género musical en el sistema.
 * Contiene la información básica de un género: identificador único y nombre.
 *
 * @author Jose Luis Espadas, Eliabe Olah, Ismael Feito
 * @version 1.0
 */
package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

// No uso builder porque son sólamente 2 atributos
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Genre {
    /**
     * Identificador único del género en la base de datos.
     * Valor generado automáticamente por la base de datos.
     */
    private int genreId;

    /**
     * Nombre del género musical.
     * Ejemplos: "Rock", "Jazz", "Clásica", "Electrónica"
     */
    private String name;

    /**
     * Constructor específico para crear géneros con solo el nombre.
     * El ID se generará automáticamente en la base de datos mediante autoincrement.
     *
     * @param name Nombre del género a crear
     */
    public Genre(String name) {
        this.name = name;
    }
}