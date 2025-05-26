package com.lab7.server;

import com.lab7.server.commands.*;
import com.lab7.server.commands.askingCommands.Add;
import com.lab7.server.commands.askingCommands.AddIfMin;
import com.lab7.server.commands.askingCommands.Update;
import com.lab7.server.managers.CollectionManagerProxy;
import com.lab7.server.managers.CommandManager;
import com.lab7.server.managers.ThreadManager;
import com.lab7.server.managers.Executer;
import com.lab7.server.managers.ServerNetworkManager;
import com.lab7.server.utility.CommandNames;

import com.lab7.common.utility.ExecutionStatus;

import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

//Вариант 7995
public final class Server {
    public static final Logger logger = Logger.getLogger(Server.class.getName());

    private static void initLogger() {
        try {
            // Настройка ConsoleHandler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(getFormatter());

            // Настройка FileHandler
            FileHandler fileHandler = new FileHandler("server_logs.log", true); // true для добавления логов в конец файла
            fileHandler.setFormatter(new SimpleFormatter());

            // Добавление обработчиков в логгер
            Server.logger.setUseParentHandlers(false);
            Server.logger.addHandler(consoleHandler);
            Server.logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize file handler for logger: " + e.getMessage());
        }
    }

    private static Formatter getFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                String color = switch (record.getLevel().getName()) {
                    case "SEVERE" -> "\u001B[31m"; // Красный
                    case "WARNING" -> "\u001B[33m"; // Желтый
                    case "INFO" -> "\u001B[32m"; // Зеленый
                    default -> "\u001B[0m"; // Сброс цвета
                };
                return color + "[" + record.getLevel() + "] " +
                        "[" + Thread.currentThread().getName() + "] " +
                        "[" + new Date(record.getMillis()) + "] " +
                        formatMessage(record) + "\u001B[0m\n";
            }
        };
    }

    public static void main(String[] args) {
        initLogger();
        ExecutionStatus loadStatus = CollectionManagerProxy.getInstance().loadCollection();
        ServerNetworkManager networkManager = ServerNetworkManager.getInstance();
        if (!loadStatus.isSuccess()) {
            logger.severe(loadStatus.getMessage());
            System.exit(1);
        }
        logger.info("The collection has been successfully loaded!");

        // Регистрация команд
        CommandManager commandManager = new CommandManager() {{
            register(CommandNames.HELP.getName(), new Help(this));
            register(CommandNames.INFO.getName(), new Info());
            register(CommandNames.SHOW.getName(), new Show());
            register(CommandNames.ADD.getName(), new Add());
            register(CommandNames.UPDATE.getName(), new Update());
            register(CommandNames.REMOVE_BY_ID.getName(), new RemoveById());
            register(CommandNames.CLEAR.getName(), new Clear());
            register(CommandNames.EXECUTE_SCRIPT.getName(), new ExecuteScript());
            register(CommandNames.EXIT.getName(), new Exit());
            register(CommandNames.REMOVE_FIRST.getName(), new RemoveFirst());
            register(CommandNames.ADD_IF_MIN.getName(), new AddIfMin());
            register(CommandNames.SORT.getName(), new Sort());
            register(CommandNames.REMOVE_ALL_BY_GENRE.getName(), new RemoveAllByGenre());
            register(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), new PrintFieldAscendingDescription());
            register(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), new PrintFieldDescendingDescription());
        }};

        Executer executer = new Executer(commandManager);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                networkManager.close();
            } catch (Exception e) {
                logger.severe("An error occurred while shutting down the server: " + e.getMessage());
            }
            finally {
                logger.info("Server shutdown complete.");
            }
        }));

        try {
            ThreadManager.getInstance().runServer(commandManager, executer);
        } catch (ClosedSelectorException e) {
            logger.warning("Selector was closed.");
        } catch (IOException | NullPointerException e) {
            logger.severe("Error while running the server: " + e.getMessage());
        }
    }
    /*
    todo за респект: проверка сначала привилегию пользователя на удаление и изменение, а потом на изменение этого объекта
    todo за респект: аутентификации сделать через декоратор
    todo за респект: для пользователя сделать роли и права доступа(админ (может назначать и убирать права пользователей), обычный пользователь)
    */
}