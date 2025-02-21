package models;

public enum MusicGenre {
    JAZZ,
    MATH_ROCK,
    BRIT_POP;

    public static String list() {
        StringBuilder list = new StringBuilder();
        for (MusicGenre genre : MusicGenre.values()) {
            list.append(genre).append(", ");
        }
        return list.substring(0, list.length() - 2);
    }
}
