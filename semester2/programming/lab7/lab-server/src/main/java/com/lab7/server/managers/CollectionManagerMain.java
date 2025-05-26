package com.lab7.server.managers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.concurrent.locks.ReadWriteLock;

import com.lab7.common.models.MusicBand;
import com.lab7.common.models.MusicGenre;
import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;

import com.lab7.server.Server;

/**
 * Класс, управляющий коллекцией музыкальных групп.
 */
class CollectionManagerMain implements CollectionManager {
    private static volatile CollectionManagerMain instance;
    private final DBManagerInterface dbManager = DBManager.getInstance();
    private final Map<Long, MusicBand> bandsMap = new HashMap<>();
    private Stack<MusicBand> collection = new Stack<>();
    private LocalDateTime initializationDate;
    private LocalDateTime lastSaveDate;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Конструктор класса CollectionManagerMain.
     */
    private CollectionManagerMain() {
    }

    /**
     * Возвращает единственный экземпляр CollectionManagerMain.
     *
     * @return Экземпляр CollectionManagerMain.
     */
    public static CollectionManagerMain getInstance() {
        if (instance == null) {
            synchronized (CollectionManagerMain.class) {
                if (instance == null) {
                    instance = new CollectionManagerMain();
                }
            }
        }
        return instance;
    }

    /**
     * Возвращает коллекцию музыкальных групп.
     *
     * @return Коллекция музыкальных групп.
     */
    @Override
    public Stack<MusicBand> getBands() {
        lock.readLock().lock(); // Блокировка чтения
        try {
            return collection;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Сортирует коллекцию музыкальных групп.
     */
    @Override
    public void sort() {
        // Коллекция в бд хранятся уже в отсортированном виде, поэтому там ничего не делаем
        lock.writeLock().lock();
        try {
            collection.sort(Comparator.comparing(MusicBand::getId));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Удаляет первую музыкальную группу из коллекции.
     */
    @Override
    public ExecutionStatus removeFirst(Pair<String, String> user) {
        lock.writeLock().lock();
        try {
            ExecutionStatus removeStatus = dbManager.removeById(collection.pop().getId(), user);
            if (removeStatus.isSuccess()) {
                lastSaveDate = LocalDateTime.now();
                collection.stream().findFirst().ifPresent(band -> {
                    bandsMap.remove(band.getId());
                    collection = collection.stream().skip(1).collect(Collectors.toCollection(Stack::new));
                });
            }
            return removeStatus;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Возвращает дату инициализации коллекции.
     *
     * @return Дата инициализации коллекции.
     */
    @Override
    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    /**
     * Возвращает дату последнего сохранения коллекции.
     *
     * @return Дата последнего сохранения коллекции.
     */
    @Override
    public LocalDateTime getLastSaveDate() {
        lock.readLock().lock(); // Блокировка чтения
        try {
            return lastSaveDate;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает коллекцию музыкальных групп.
     *
     * @return Коллекция музыкальных групп.
     */
    @Override
    public Stack<MusicBand> getCollection() {
        lock.readLock().lock(); // Блокировка чтения
        try {
            return collection;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает музыкальную группу по идентификатору.
     *
     * @param id Идентификатор музыкальной группы.
     * @return Музыкальная группа с указанным идентификатором.
     */
    @Override
    public MusicBand getById(Long id) {
        lock.readLock().lock(); // Блокировка чтения
        try {
            return bandsMap.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Удаляет все музыкальные группы с указанным жанром.
     *
     * @param genre Жанр музыкальных групп для удаления.
     * @return Количество удалённых групп.
     */
    @Override
    public ExecutionStatus removeAllByGenre(MusicGenre genre, Pair<String, String> user) {
        lock.writeLock().lock();
        try {
            ExecutionStatus removeStatus = dbManager.removeAllByGenre(genre, user);
            if (removeStatus.isSuccess()) {
                lastSaveDate = LocalDateTime.now();
                collection = collection.stream().filter(band -> !band.getGenre().equals(genre) || !band.getUser().equals(user.getFirst()))
                        .collect(Collectors.toCollection(Stack::new));
                bandsMap.entrySet().removeIf(entry -> entry.getValue().getGenre().equals(genre));
            }
            return removeStatus;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Загружает коллекцию музыкальных групп.
     *
     * @return Статус выполнения загрузки коллекции.
     */
    @Override
    public ExecutionStatus loadCollection() {
        lock.writeLock().lock();
        try {
            collection.clear();
            bandsMap.clear();
            ExecutionStatus loadStatus = dbManager.loadCollection(collection);
            if (loadStatus.isSuccess()) {
                initializationDate = LocalDateTime.now();
                lastSaveDate = LocalDateTime.now();
                boolean hasDuplicates = collection.stream().anyMatch(band -> bandsMap.putIfAbsent(band.getId(), band) != null);
                if (hasDuplicates) {
                    return new ExecutionStatus(false, "Ошибка загрузки коллекции: обнаружены дубликаты id!");
                }
            }
            return loadStatus;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Очищает коллекцию музыкальных групп.
     */
    @Override
    public ExecutionStatus clear(Pair<String, String> user) {
        lock.writeLock().lock();
        try {
            ExecutionStatus clearStatus = dbManager.clear(user);
            if (clearStatus.isSuccess()) {
                collection = collection.stream()
                        .filter(band -> !band.getUser().equals(user.getFirst()))
                        .collect(Collectors.toCollection(Stack::new));
                bandsMap.entrySet().removeIf(entry -> entry.getValue().getUser().equals(user.getFirst()));
            }
            return clearStatus;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public ExecutionStatus add(MusicBand band, Pair<String, String> user) {
        lock.writeLock().lock();
        try {
            if ((band != null) && band.validate()) {
                ExecutionStatus addStatus = dbManager.addMusicBand(band, user);
                if (addStatus.isSuccess()) {
                    band.updateId(Long.parseLong(addStatus.getMessage()));
                    lastSaveDate = LocalDateTime.now();
                    collection.push(band);
                    bandsMap.put(band.getId(), band);
                    return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию! Присвоенный id = " + addStatus.getMessage());
                }
                return new ExecutionStatus(false, "Произошла ошибка при добавлении коллекции в базу данных!");
            }
            return new ExecutionStatus(false, "Элемент коллекции введён неверно!");
        } catch (SQLException e) {
            return new ExecutionStatus(false, "Ошибка при сохранении элемента коллекции в базу данных: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public ExecutionStatus update(MusicBand band, Pair<String, String> user) {
        lock.writeLock().lock();
        try {
            ExecutionStatus updStatus = dbManager.updateMusicBand(band, user);
            if (updStatus.isSuccess()) {
                lastSaveDate = LocalDateTime.now();
                collection.stream()
                        .filter(existingBand -> existingBand.getId().equals(band.getId()))
                        .forEach(existingBand -> {
                            bandsMap.remove(existingBand.getId(), existingBand);
                            existingBand.updateName(band.getName());
                            existingBand.updateCoordinates(band.getCoordinates());
                            existingBand.updateNumberOfParticipants(band.getNumberOfParticipants());
                            existingBand.updateAlbumsCount(band.getAlbumsCount());
                            existingBand.updateDescription(band.getDescription());
                            existingBand.updateGenre(band.getGenre());
                            existingBand.updateStudio(band.getStudio());
                            bandsMap.put(band.getId(), band);
                        });

            } else {
                Server.logger.severe("Error updating band in database: " + band.getName());
            }
            return updStatus;
        } catch (SQLException e) {
            return new ExecutionStatus(false, "Ошибка при сохранении элемента коллекции в базу данных: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Удаляет музыкальную группу по идентификатору.
     *
     * @param elementId Идентификатор музыкальной группы для удаления.
     */
    @Override
    public ExecutionStatus removeById(Long elementId, Pair<String, String> user) {
        lock.writeLock().lock();
        try {
            ExecutionStatus removeStatus = dbManager.removeById(elementId, user);
            if (removeStatus.isSuccess()) {
                collection = collection.stream().filter(band -> !band.getId().equals(elementId)).collect(Collectors.toCollection(Stack::new));
                bandsMap.remove(elementId);
            }
            return removeStatus;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
