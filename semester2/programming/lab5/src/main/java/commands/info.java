package commands;

import managers.*;
import utility.*;

public class info extends Command{
    Console console;
    CollectionManager collectionManager;

    public info(Console console, CollectionManager collectionManager){
        super("info", "вывести в стандартный поток вывода информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    @Override
    public ExecutionStatus run(String arg) {
        if (!arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды add ввод аргументов построчный!\nПример корректного ввода: " + getName());
        }
        console.println("Тип коллекции: " + collectionManager.getBands().getClass().getName());
        console.println("Дата инициализации: " + collectionManager.getInitializationDate());
        console.println("Дата последнего сохранения: " + collectionManager.getLastSaveDate());
        console.println("Количество элементов: " + collectionManager.getBands().size());
        return new ExecutionStatus(true, "Информация о коллекции успешно выведена!");
    }
}
