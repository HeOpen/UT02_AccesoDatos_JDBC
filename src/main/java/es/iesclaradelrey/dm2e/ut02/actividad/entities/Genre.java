package es.iesclaradelrey.dm2e.ut02.actividad.entities;

import lombok.*;

// No uso builder porque son sólamente 2 atributos
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Genre {
    private int genreId;
    private String name;

    // Constructor específico para solo introducir un nombre, el ID lo da autoincrement
    public Genre(String name) {
        this.name = name;
    }
}
