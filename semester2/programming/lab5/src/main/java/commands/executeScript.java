package commands;

import utility.*;


public class executeScript extends Command {
    public final Console console;

    public executeScript(Console console) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла.");
        this.console = console;
    }

    @Override
    public ExecutionStatus run(String arg) {
        return null;
    }
}

