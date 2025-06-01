package com.lab8.common.validators;

import com.lab8.common.utility.ExecutionStatus;
import com.lab8.common.utility.PermissionType;

import java.io.Serial;
import java.io.Serializable;

public class UserPermissionValidator extends ArgumentValidator implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    @Override
    public ExecutionStatus validate(String arg, String name) {
        String[] args = arg.split(" ");
        if (args.length != 2) {
            return new ExecutionStatus(false, "Неверное количество аргументов. Используйте: update_user_permission <username> <new_permission>");
        }
        try {
            PermissionType.valueOf(args[1]);
            return new ExecutionStatus(true, "Аргумент команды введен корректно.");
        } catch (IllegalArgumentException e) {
            return new ExecutionStatus(false, "Неверный тип прав. Доступные типы: " + PermissionType.getAllPermissions());
        }
    }
}
