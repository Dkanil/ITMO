package commands;

import utility.*;

/**
 * Абстрактный класс для всех команд.
 */
public abstract class Command {
    private final Pair<String, String> nameAndDescription;
    protected Console console;

    /**
     * Конструктор команды.
     * @param name Имя команды.
     * @param description Описание команды.
     * @param console Консоль ввода-вывода.
     */
    public Command(String name, String description, Console console) {
        this.nameAndDescription = new Pair<String, String>(name, description);
        this.console = console;
    }

    /**
     * Возвращает имя команды.
     * @return Имя команды.
     */
    public String getName() {
        return nameAndDescription.getFirst();
    }

    /**
     * Возвращает описание команды.
     * @return Описание команды.
     */
    public String getDescription() {
        return nameAndDescription.getSecond();
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
     * Приватный метод для выполнения команды с проверкой. В случае успешной проверки вызывает метод runInternal.
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    public ExecutionStatus run(String arg){
        ExecutionStatus ArgumentStatus = validate(arg, getName());
        if (ArgumentStatus.isSuccess()) {
            return runInternal(arg);
        } else {
            return ArgumentStatus;
        }
    }

    /**
     * Абстрактный метод для выполнения внутренней части команды.
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    protected abstract ExecutionStatus runInternal(String arg);

    /**
     * Возвращает хэш-код команды.
     * @return Хэш-код команды.
     */
    @Override
    public int hashCode() {
        return nameAndDescription.getFirst().hashCode() + nameAndDescription.getSecond().hashCode();
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
        return nameAndDescription.getFirst().equals(command.nameAndDescription.getFirst()) && nameAndDescription.getSecond().equals(command.nameAndDescription.getSecond());
    }

    /**
     * Возвращает строковое представление команды.
     * @return Строковое представление команды.
     */
    @Override
    public String toString() {
        return "Command{" +
                "name='" + nameAndDescription.getFirst() + '\'' +
                ", description='" + nameAndDescription.getSecond() + '\'' +
                '}';
    }
}