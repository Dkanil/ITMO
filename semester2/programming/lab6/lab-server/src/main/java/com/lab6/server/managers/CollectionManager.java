package com.lab6.server.managers;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.List;
import java.util.stream.Collectors;

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
        collection = collection.stream()
                .sorted(Comparator.comparing(MusicBand::getId))
                .collect(Collectors.toCollection(Stack::new));
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
        List<MusicBand> toRemove = collection.stream()
                .filter(band -> band.getGenre().equals(genre))
                .collect(Collectors.toList());
        toRemove.forEach(band -> Bands.remove(band.getId()));
        collection = collection.stream()
                .filter(band -> !band.getGenre().equals(genre))
                .collect(Collectors.toCollection(Stack::new));
        return toRemove.size();
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

        boolean hasDuplicates = collection.stream()
                .anyMatch(band -> Bands.putIfAbsent(band.getId(), band) != null);

        if (hasDuplicates) {
            return new ExecutionStatus(false, "Ошибка загрузки коллекции: обнаружены дубликаты id!");
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
        collection = collection.stream()
                .filter(band -> !band.getId().equals(elementId))
                .collect(Collectors.toCollection(Stack::new));
        Bands.remove(elementId);
    }
}
