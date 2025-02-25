package managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import models.MusicBand;

public class CollectionManager {
    private Long id = 1L;
    private final DumpManager dumpManager;
    private Map<Long, MusicBand> Bands = new HashMap<>();
    private Stack<MusicBand> collection = new Stack<MusicBand>();

    /**
     * Конструктор класса CollectionManager.
     * @param dumpManager Менеджер для сохранения и загрузки коллекции.
     */
    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
        loadCollection();
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
    }

    /**
     * Загружает коллекцию музыкальных групп.
     */
    public void loadCollection() {
        collection.clear();
        Bands.clear();
        dumpManager.ReadCollection(collection);
        for (MusicBand band : collection) {
            Bands.put(band.getId(), band); // TODO: добавить проверку на уникальность id
        }
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
}