package com.lab6.common.validators;

import com.lab6.common.utility.ExecutionStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * Валидатор для проверки отсутствия аргументов у команды.
 */
public class EmptyValidator extends ArgumentValidator implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
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
