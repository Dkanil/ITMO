package commands;

import utility.*;

public class exit extends Command {
    Console console;
    public exit(Console console) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.console = console;
    }
    @Override
    public ExecutionStatus run(String arg) {
        if (!arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды exit нет аргументов!!\nПример корректного ввода: " + getName());
        }
        System.exit(0);
        return new ExecutionStatus(true, "Программа завершена!");
    }
}
