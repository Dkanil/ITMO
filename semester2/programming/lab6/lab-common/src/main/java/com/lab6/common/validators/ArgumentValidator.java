package com.lab6.common.validators;

import com.lab6.common.utility.ExecutionStatus;

/**
 * Абстрактный класс для валидаторов аргументов команд.
 */
public abstract class ArgumentValidator {
    /**
     * Проверяет аргумент команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    public abstract ExecutionStatus validate(String arg, String name);
}
