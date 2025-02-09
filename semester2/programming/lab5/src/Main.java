import models.*;

import java.util.Stack;

public class Main {
    static Stack<MusicBand> Bands = new Stack<MusicBand>();
    public static void main(String[] args) {
        Bands.push(new MusicBand(1L, "Band1", java.time.LocalDateTime.now(), 5L, "Description1", new Coordinates(1.0, 1), 1L, MusicGenre.JAZZ, new Studio("Studio1", "Address1")));
        Bands.push(new MusicBand(2L, "Band2", java.time.LocalDateTime.now(), 5L, "Description2", new Coordinates(2.0, 2), 2L, MusicGenre.MATH_ROCK, new Studio("Studio2", "Address2")));

        for (MusicBand band : Bands) {
            System.out.println(band);
        }
    }
}

// TODO: 1. Добавить автогенерацию id
// TODO: 2. Добавить проверку уникальности id
// TODO: 3. Добавить автогенерацию creationDate