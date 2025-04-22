package com.lab6.common.validators;

import com.lab6.common.utility.ExecutionStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * Валидатор для проверки корректности идентификатора элемента коллекции.
 */
public class IdValidator extends ArgumentValidator implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    /**
     * Проверяет корректность аргумента команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        if (arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды должен быть аргумент (id элемента коллекции)!\nПример корректного ввода: " + name);
        }
        try {
            Long id = Long.parseLong(arg);
        } catch (NumberFormatException e) {
            return new ExecutionStatus(false, "Формат аргумента неверен! Он должен быть целым числом.");
        }
        return new ExecutionStatus(true, "Аргумент команды введен корректно.");
    }
}
