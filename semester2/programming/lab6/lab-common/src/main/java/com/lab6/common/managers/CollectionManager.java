package com.lab6.common.managers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import com.lab6.common.models.MusicBand;
import com.lab6.common.models.MusicGenre;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс, управляющий коллекцией музыкальных групп.
 */
public class CollectionManager {
    private Long id = 1L;
    private final DumpManager dumpManager;
    private Map<Long, MusicBand> Bands = new HashMap<>();
    private Stack<MusicBand> collection = new Stack<>();
    private LocalDateTime InitializationDate;
    private LocalDateTime lastSaveDate;

    /**
     * Конструктор класса CollectionManager.
     * @param dumpManager Менеджер для сохранения и загрузки коллекции.
     */
    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
    }

    /**
     * Возвращает коллекцию музыкальных групп.
     * @return Коллекция музыкальных групп.
     */
    public Stack<MusicBand> getBands() {
        return collection;
    }

    /**
     * Сортирует коллекцию музыкальных групп.
     */
    public void sort() {
        Stack<MusicBand> sortedBands = new Stack<>();
        Map<Long, MusicBand> SortedBandsMap = new TreeMap<>(Bands);
        for (var band : SortedBandsMap.entrySet()) {
            sortedBands.push(band.getValue());
        }
        collection = sortedBands;
    }

    /**
     * Удаляет первую музыкальную группу из коллекции.
     */
    public void removeFirst() {
        if (!collection.isEmpty()) {
            Bands.remove(collection.firstElement().getId());
            collection.remove(0);
        }
    }

    /**
     * Возвращает свободный идентификатор.
     * @return Свободный идентификатор.
     */
    public Long getFreeId() {
        while (Bands.containsKey(id)) {
            id++;
        }
        return id;
    }

    /**
     * Возвращает дату инициализации коллекции.
     * @return Дата инициализации коллекции.
     */
    public LocalDateTime getInitializationDate() {
        return InitializationDate;
    }

    /**
     * Возвращает дату последнего сохранения коллекции.
     * @return Дата последнего сохранения коллекции.
     */
    public LocalDateTime getLastSaveDate() {
        return lastSaveDate;
    }

    /**
     * Возвращает коллекцию музыкальных групп.
     * @return Коллекция музыкальных групп.
     */
    public Stack<MusicBand> getCollection() {
        return collection;
    }

    /**
     * Возвращает музыкальную группу по идентификатору.
     * @param id Идентификатор музыкальной группы.
     * @return Музыкальная группа с указанным идентификатором.
     */
    public MusicBand getById(Long id) {
        return Bands.get(id);
    }

    /**
     * Удаляет все музыкальные группы с указанным жанром.
     * @param genre Жанр музыкальных групп для удаления.
     * @return Количество удалённых групп.
     */
    public int removeAllByGenre(MusicGenre genre) {
        int count = 0;
        for (MusicBand band : collection) {
            if (band.getGenre().equals(genre)) {
                Bands.remove(band.getId());
                count++;
            }
        }
        collection.removeIf(band -> band.getGenre().equals(genre));
        return count;
    }

    /**
     * Сохраняет коллекцию музыкальных групп.
     */
    public void saveCollection() {
        dumpManager.WriteCollection(collection);
        lastSaveDate = LocalDateTime.now();
    }

    /**
     * Загружает коллекцию музыкальных групп.
     * @return Статус выполнения загрузки коллекции.
     */
    public ExecutionStatus loadCollection() {
        collection.clear();
        Bands.clear();
        dumpManager.ReadCollection(collection);
        InitializationDate = LocalDateTime.now();
        lastSaveDate = LocalDateTime.now();
        for (MusicBand band : collection) {
            if (getById(band.getId()) != null) {
                return new ExecutionStatus(false, "Ошибка загрузки коллекции: обнаружены дубликаты id!");
            }
            Bands.put(band.getId(), band);
        }
        return new ExecutionStatus(true, "Коллекция успешно загружена!");
    }

    /**
     * Очищает коллекцию музыкальных групп.
     */
    public void clear() {
        collection.clear();
        Bands.clear();
    }

    /**
     * Добавляет музыкальную группу в коллекцию.
     * @param band Музыкальная группа для добавления.
     * @return true, если группа успешно добавлена, иначе false.
     */
    public boolean add(MusicBand band) {
        if ((band != null) && band.validate() && !Bands.containsKey(band.getId())) {
            collection.push(band);
            Bands.put(band.getId(), band);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Удаляет музыкальную группу по идентификатору.
     * @param elementId Идентификатор музыкальной группы для удаления.
     */
    public void removeById(Long elementId) {
        MusicBand band = Bands.get(elementId);
        if (band != null) {
            collection.remove(band);
            Bands.remove(elementId);
            id = elementId;
        }
    }
}
