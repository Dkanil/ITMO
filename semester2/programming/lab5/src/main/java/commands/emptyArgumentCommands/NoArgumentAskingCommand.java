package commands.emptyArgumentCommands;

import managers.Asker;
import managers.CollectionManager;
import models.MusicBand;
import utility.*;

public abstract class NoArgumentAskingCommand extends NoArgumentCommand {
    CollectionManager collectionManager;
    public NoArgumentAskingCommand(String name, String description, Console console, CollectionManager collectionManager) {
        super(name, description, console);
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionStatus run(String arg){
        ExecutionStatus ArgumentStatus = validate(arg, getName());
        if (ArgumentStatus.isSuccess()) {
            console.println("Добавление элемента в коллекцию...");
            Pair<ExecutionStatus, MusicBand> validationStatusPair = validAsking(console, collectionManager.getFreeId());
            if (validationStatusPair.getFirst().isSuccess()) {
                return runInternal(validationStatusPair);
            }
            return validationStatusPair.getFirst();
        } else {
            return ArgumentStatus;
        }
    }

    @Override
    public ExecutionStatus runInternal(String arg) {
        return null;
    }
    public abstract ExecutionStatus runInternal(Pair<ExecutionStatus, MusicBand> validationStatusPair);

    public Pair<ExecutionStatus, MusicBand> validAsking(Console console, Long id) {
        try {
            MusicBand band = Asker.askBand(console, id);
            if (band != null && band.validate()) {
                return new Pair<ExecutionStatus, MusicBand>(new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!"), band);
            }
            return new Pair<ExecutionStatus, MusicBand>(new ExecutionStatus(false, "Введены некорректные данные!"), null);
        } catch (Asker.Breaker e) {
            return new Pair<ExecutionStatus, MusicBand>(new ExecutionStatus(false, "Ввод был прерван пользователем!"), null);
        } catch (Asker.IllegalInputException e) {
            return new Pair<ExecutionStatus, MusicBand>(new ExecutionStatus(false, e.getMessage()), null);
        }
    }
}
