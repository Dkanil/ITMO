import models.*;
import utility.*;

import java.util.Stack;
import managers.*;
import utility.*;
import commands.*;
import models.*;

// Вариант 4192

public class Main {
    static Stack<MusicBand> Bands = new Stack<MusicBand>();
    public static void main(String[] args) {
        var console = new StandartConsole();
        DumpManager dumpManager = new DumpManager("dump.csv", console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);

        CommandManager commandManager = new CommandManager() {{
            register("add", new add(console, collectionManager));
            register("execute_script", new executeScript(console));
        }};
        new Executer(console).interactiveMode();
    }
}