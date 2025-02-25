package managers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import models.MusicBand;
import utility.ExecutionStatus;

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
     * Возвращает свободный идентификатор.
     * @return Свободный идентификатор.
     */
    public Long getFreeId() {
        while (Bands.containsKey(id)) {
            id++;
        }
        return id;
    }

    public LocalDateTime getInitializationDate() {
        return InitializationDate;
    }

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
     * Сохраняет коллекцию музыкальных групп.
     */
    public void saveCollection() {
        dumpManager.WriteCollection(collection);
        lastSaveDate = LocalDateTime.now();
    }

    /**
     * Загружает коллекцию музыкальных групп.
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
     * Добавляет музыкальную группу в коллекцию.
     * @param band Музыкальная группа для добавления.
     * @return true, если группа успешно добавлена, иначе false.
     */
    public boolean add(MusicBand band) {
        if ((band != null) && band.validate() && !Bands.containsKey(band.getId())) {
            collection.push(band);
            Bands.put(band.getId(), band);
            return true;
        }
        else {
            return false;
        }
    }

    public void removeById(Long elementId) {
        MusicBand band = Bands.get(elementId);
        if (band != null) {
            collection.remove(band);
            Bands.remove(elementId);
            id = elementId;
        }
    }
}