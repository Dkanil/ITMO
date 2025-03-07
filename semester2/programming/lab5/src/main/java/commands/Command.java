package commands;

import utility.Console;
import utility.ExecutionStatus;

/**
 * Абстрактный класс для всех команд.
 */
public abstract class Command {
    private final String name;
    private final String description;
    protected Console console;

    /**
     * Конструктор команды.
     * @param name Имя команды.
     * @param description Описание команды.
     * @param console Консоль ввода-вывода.
     */
    public Command(String name, String description, Console console) {
        this.name = name;
        this.description = description;
        this.console = console;
    }

    /**
     * Возвращает имя команды.
     * @return Имя команды.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды.
     * @return Описание команды.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Обновляет консоль ввода-вывода.
     * @param console Консоль ввода-вывода.
     */
    public void updateConsole(Console console) {
        this.console = console;
    }

    public abstract ExecutionStatus validate(String arg, String name);

    /**
     * Абстрактный метод для выполнения команды.
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    public abstract ExecutionStatus run(String arg);

    /**
     * Возвращает хэш-код команды.
     * @return Хэш-код команды.
     */
    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    /**
     * Сравнивает текущую команду с другим объектом.
     * @param object Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Command command = (Command) object;
        return name.equals(command.name) && description.equals(command.description);
    }

    /**
     * Возвращает строковое представление команды.
     * @return Строковое представление команды.
     */
    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}