package com.lab8.common.models;

/**
 * Перечисление, представляющее музыкальные жанры.
 */
public enum MusicGenre {
    JAZZ,
    MATH_ROCK,
    BRIT_POP;

    /**
     * Возвращает строку, содержащую все музыкальные жанры, разделенные запятыми.
     * @return строка с перечислением всех музыкальных жанров
     */
    public static String list() {
        StringBuilder list = new StringBuilder();
        for (MusicGenre genre : MusicGenre.values()) {
            list.append(genre).append(", ");
        }
        return list.substring(0, list.length() - 2);
    }
}
