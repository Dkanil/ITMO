package commands;

import managers.*;
import models.MusicBand;
import utility.*;

/**
 * Класс команды для обновления значения элемента коллекции по его id.
 */
public class update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды update.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public update(Console console, CollectionManager collectionManager) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду обновления элемента коллекции.
     * @param arg Аргумент команды, содержащий id элемента.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String arg) {
        try {
            Long id = Long.parseLong(arg);
            if (arg.isEmpty()) {
                return new ExecutionStatus(false, "У команды update должен быть аргумент - id элемента коллекции!\nПример корректного ввода: " + getName());
            }
            if (collectionManager.getById(id) == null) {
                return new ExecutionStatus(false, "Элемент с указанным id не найден!");
            }
            console.println("Обновление элемента коллекции...");
            MusicBand band = Asker.askBand(console, id);

            if (band != null && band.validate()) {
                collectionManager.removeById(id);
                collectionManager.add(band);
            } else {
                return new ExecutionStatus(false, "Введены некорректные данные!");
            }
        } catch (Asker.Breaker e) {
            return new ExecutionStatus(false, "Ввод был прерван пользователем!");
        } catch (NumberFormatException e) {
            return new ExecutionStatus(false, "id должен быть целым числом!");
        }

        return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
    }
}