package com.lab7.server.utility;

import com.lab7.common.utility.Pair;

/**
 * Перечисление, представляющее имена команд и их описания.
 */
public enum CommandNames {
    HELP("help", "вывести справку по доступным командам"),
    INFO("info", "вывести в стандартный поток вывода информацию о коллекции"),
    SHOW("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении"),
    ADD("add", "добавить новый элемент в коллекцию"),
    UPDATE("update", "обновить значение элемента коллекции, id которого равен заданному"),
    REMOVE_BY_ID("remove_by_id", "удалить элемент из коллекции по его id"),
    CLEAR("clear", "очистить коллекцию"),
    EXECUTE_SCRIPT("execute_script", "считать и исполнить скрипт из указанного файла"),
    EXIT("exit", "завершить программу (без сохранения в файл)"),
    REMOVE_FIRST("remove_first", "удалить первый элемент из коллекции"),
    ADD_IF_MIN("add_if_min", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции"),
    SORT("sort", "отсортировать коллекцию в естественном порядке"),
    REMOVE_ALL_BY_GENRE("remove_all_by_genre", "удалить из коллекции все элементы, значение поля genre которого эквивалентно заданному"),
    PRINT_FIELD_ASCENDING_DESCRIPTION("print_field_ascending_description", "вывести значения поля description всех элементов в порядке возрастания"),
    PRINT_FIELD_DESCENDING_DESCRIPTION("print_field_descending_description", "вывести значения поля description всех элементов в порядке убывания");

    private final Pair<String, String> commandDescription;

    CommandNames(String command, String description) {
        this.commandDescription = new Pair<>(command, description);
    }

    public String getName() {
        return commandDescription.getFirst();
    }

    public String getDescription() {
        return commandDescription.getSecond();
    }
}
