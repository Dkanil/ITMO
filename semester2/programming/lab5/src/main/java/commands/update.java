package commands;

import managers.*;
import models.MusicBand;
import utility.*;

public class update extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public update(Console console, CollectionManager collectionManager){
        super("update", "обновить значение элемента коллекции, id которого равен заданному");
        this.console = console;
        this.collectionManager = collectionManager;
    }
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

            if (band != null && band.validate()){
                collectionManager.removeById(id);
                collectionManager.add(band);
            }
            else {
                return new ExecutionStatus(false, "Введены некорректные данные!");
            }
        } catch (Asker.Breaker e) {
            return new ExecutionStatus(false, "Ввод был прерван пользователем!");
        } catch (NumberFormatException e) {
            return new ExecutionStatus(false, "id должен быть целым числом!");
        }

        try {
            collectionManager.saveCollection();
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при сохранении коллекции!");
        }
        return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
    }
}
