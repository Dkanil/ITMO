package managers;

import java.time.LocalDateTime;
import java.util.Stack;
import models.MusicBand;
import utility.Console;

public class CollectionManager {
    private LocalDateTime lastSave;
    private LocalDateTime lastInit;
    private final DumpManager dumpManager;
    private Stack<MusicBand> collection = new Stack<MusicBand>();

    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
        this.lastSave = LocalDateTime.now();
        this.lastInit = LocalDateTime.now();
    }

    public LocalDateTime getLastSave() {
        return lastSave;
    }

    public LocalDateTime getLastInit() {
        return lastInit;
    }

    public Stack<MusicBand> getBands() {
        return collection;
    }

    public void add(MusicBand band) {
        if ((band != null) && (band.validate())) {
            collection.push(band);
        }
    }
}
