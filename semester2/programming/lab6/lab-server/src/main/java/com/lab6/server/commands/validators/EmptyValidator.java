package com.lab6.server.commands.validators;

import com.lab6.common.utility.ExecutionStatus;

/**
 * Валидатор для проверки отсутствия аргументов у команды.
 */
public class EmptyValidator extends ArgumentValidator {
    /**
     * Проверяет корректность аргумента команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        if (!arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды нет аргументов!\nПример корректного ввода: " + name);
        }
        return new ExecutionStatus(true, "Аргумент команды введен корректно.");
    }
}
