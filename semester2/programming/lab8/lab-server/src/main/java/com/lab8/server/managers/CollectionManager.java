package com.lab8.server.managers;

import com.lab8.common.models.MusicBand;
import com.lab8.common.models.MusicGenre;
import com.lab8.common.utility.ExecutionStatus;
import com.lab8.common.utility.Pair;

import java.time.LocalDateTime;
import java.util.Stack;

/**
 * Интерфейс CollectionManager предоставляет методы для управления коллекцией музыкальных групп.
 */
public interface CollectionManager {

    /**
     * Возвращает коллекцию музыкальных групп.
     *
     * @return Коллекция музыкальных групп.
     */
    Stack<MusicBand> getBands();

    /**
     * Сортирует коллекцию музыкальных групп.
     */
    void sort();

    /**
     * Удаляет первую музыкальную группу из коллекции.
     */
    ExecutionStatus removeFirst(Pair<String, String> user);

    /**
     * Возвращает дату инициализации коллекции.
     *
     * @return Дата инициализации коллекции.
     */
    LocalDateTime getInitializationDate();

    /**
     * Возвращает дату последнего сохранения коллекции.
     *
     * @return Дата последнего сохранения коллекции.
     */
    LocalDateTime getLastSaveDate();

    /**
     * Возвращает коллекцию музыкальных групп.
     *
     * @return Коллекция музыкальных групп.
     */
    Stack<MusicBand> getCollection();

    /**
     * Возвращает музыкальную группу по идентификатору.
     *
     * @param id Идентификатор музыкальной группы.
     * @return Музыкальная группа с указанным идентификатором.
     */
    MusicBand getById(Long id);

    /**
     * Удаляет все музыкальные группы с указанным жанром.
     *
     * @param genre Жанр музыкальных групп для удаления.
     * @return Количество удалённых групп.
     */
    ExecutionStatus removeAllByGenre(MusicGenre genre, Pair<String, String> user);

    /**
     * Загружает коллекцию музыкальных групп.
     *
     * @return Статус выполнения загрузки коллекции.
     */
    ExecutionStatus loadCollection();

    /**
     * Очищает коллекцию музыкальных групп.
     */
    ExecutionStatus clear(Pair<String, String> user);

    /**
     * Добавляет музыкальную группу в коллекцию.
     *
     * @param band Музыкальная группа для добавления.
     * @return true, если группа успешно добавлена, иначе false.
     */
    ExecutionStatus add(MusicBand band, Pair<String, String> user);

    ExecutionStatus update(MusicBand band, Pair<String, String> user);

    /**
     * Удаляет музыкальную группу по идентификатору.
     *
     * @param elementId Идентификатор музыкальной группы для удаления.
     */
    ExecutionStatus removeById(Long elementId, Pair<String, String> user);
}
