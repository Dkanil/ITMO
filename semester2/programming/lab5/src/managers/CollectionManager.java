package managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import models.MusicBand;

public class CollectionManager {
    private Long id = 0L;
    private final DumpManager dumpManager;
    private Map<Long, MusicBand> Bands = new HashMap<>();
    private Stack<MusicBand> collection = new Stack<MusicBand>();

    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
    }

    public Stack<MusicBand> getBands() {
        return collection;
    }

    public Long getFreeId() {
        while (Bands.containsKey(id)) {
            id++;
        }
        return id;
    }

    public Stack<MusicBand> getCollection() {
        return collection;
    }

    public MusicBand getById(Long id) {
        return Bands.get(id);
    }

    public void saveCollection() {
        dumpManager.WriteCollection(collection);
    }

    public void loadCollection() {
        collection.clear();
        Bands.clear();
        dumpManager.ReadCollection(collection);
        for (MusicBand band : collection) {
            Bands.put(band.getId(), band); // TODO: добавить проверку на уникальность id
        }
    }

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
