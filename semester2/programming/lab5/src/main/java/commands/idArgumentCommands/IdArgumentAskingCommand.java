package commands.idArgumentCommands;

import managers.*;
import models.MusicBand;
import utility.*;

public abstract class IdArgumentAskingCommand extends IdArgumentCommand {
    public IdArgumentAskingCommand(String name, String description, Console console, CollectionManager collectionManager) {
        super(name, description, console, collectionManager);
    }

    @Override
    public ExecutionStatus run(String arg){
        ExecutionStatus ArgumentStatus = validate(arg, getName());
        if (ArgumentStatus.isSuccess()) {
            console.println("Добавление элемента в коллекцию...");
            Pair<ExecutionStatus, MusicBand> validationStatusPair = validAsking(console, Long.parseLong(arg));
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
