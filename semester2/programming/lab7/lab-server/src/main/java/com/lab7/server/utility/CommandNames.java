package com.lab7.server.utility;

import com.lab7.common.utility.Pair;
import com.lab7.common.utility.PermissionType;

/**
 * Перечисление, представляющее имена команд и их описания.
 */
public enum CommandNames {
    HELP("help", "вывести справку по доступным командам", PermissionType.ABOBA),
    INFO("info", "вывести в стандартный поток вывода информацию о коллекции", PermissionType.ABOBA),
    SHOW("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", PermissionType.ABOBA),
    EXECUTE_SCRIPT("execute_script", "считать и исполнить скрипт из указанного файла", PermissionType.ABOBA),
    EXIT("exit", "завершить программу (без сохранения в файл)", PermissionType.ABOBA),
    PRINT_FIELD_ASCENDING_DESCRIPTION("print_field_ascending_description", "вывести значения поля description всех элементов в порядке возрастания", PermissionType.ABOBA),
    PRINT_FIELD_DESCENDING_DESCRIPTION("print_field_descending_description", "вывести значения поля description всех элементов в порядке убывания", PermissionType.ABOBA),
    ADD("add", "добавить новый элемент в коллекцию", PermissionType.USER),
    UPDATE("update", "обновить значение элемента коллекции, id которого равен заданному", PermissionType.USER),
    REMOVE_BY_ID("remove_by_id", "удалить элемент из коллекции по его id", PermissionType.USER),
    CLEAR("clear", "очистить коллекцию", PermissionType.USER),
    REMOVE_FIRST("remove_first", "удалить первый элемент из коллекции", PermissionType.USER),
    ADD_IF_MIN("add_if_min", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", PermissionType.USER),
    REMOVE_ALL_BY_GENRE("remove_all_by_genre", "удалить из коллекции все элементы, значение поля genre которого эквивалентно заданному", PermissionType.USER),
    SORT("sort", "отсортировать коллекцию в естественном порядке", PermissionType.MODERATOR),
    SHOW_USER_LIST("show_user_list", "вывести список пользователей, зарегистрированных в системе", PermissionType.MODERATOR),
    UPDATE_USER_PERMISSION("update_user_permission", "обновить права пользователя в системе", PermissionType.ADMIN);

    private final Pair<String, String> commandDescription;
    private final PermissionType requiredPermission;

    CommandNames(String command, String description, PermissionType requiredPermission) {
        this.commandDescription = new Pair<>(command, description);
        this.requiredPermission = requiredPermission;
    }

    public String getName() {
        return commandDescription.getFirst();
    }

    public String getDescription() {
        return commandDescription.getSecond();
    }

    public PermissionType getRequiredPermission() {
        return requiredPermission;
    }
}
