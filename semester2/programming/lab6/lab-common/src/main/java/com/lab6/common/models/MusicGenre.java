package com.lab6.common.models;

import java.io.Serial;
import java.io.Serializable;

/**
 * Перечисление, представляющее музыкальные жанры.
 */
public enum MusicGenre implements Serializable {
    JAZZ,
    MATH_ROCK,
    BRIT_POP;

    @Serial
    private static final long serialVersionUID = 12L;
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
