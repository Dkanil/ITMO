package commands.validators;

import managers.CollectionManager;
import utility.ExecutionStatus;

/**
 * Валидатор для проверки корректности идентификатора элемента коллекции.
 */
public class IdValidator extends ArgumentValidator {
    private final CollectionManager collectionManager;

    /**
     * Конструктор валидатора IdValidator.
     *
     * @param collectionManager Менеджер коллекции.
     */
    public IdValidator(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Проверяет корректность аргумента команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        if (arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды должен быть аргумент (id элемента коллекции)!\nПример корректного ввода: " + name);
        }
        try {
            Long id = Long.parseLong(arg);
            if (collectionManager.getById(id) == null) {
                return new ExecutionStatus(false, "Элемент с указанным id не найден!");
            }
        } catch (NumberFormatException e) {
            return new ExecutionStatus(false, "Формат аргумента неверен! Он должен быть целым числом.");
        }
        return new ExecutionStatus(true, "Аргумент команды введен корректно.");
    }
}