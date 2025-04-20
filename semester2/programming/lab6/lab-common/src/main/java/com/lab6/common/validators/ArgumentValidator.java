package com.lab6.common.validators;

import com.lab6.common.utility.ExecutionStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * Абстрактный класс для валидаторов аргументов команд.
 */
public abstract class ArgumentValidator implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    /**
     * Проверяет аргумент команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    public abstract ExecutionStatus validate(String arg, String name);
}
