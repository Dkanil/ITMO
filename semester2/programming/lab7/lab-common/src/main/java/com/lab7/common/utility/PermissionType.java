package com.lab7.common.utility;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum PermissionType {
    ADMIN(3), // Администратор имеет право управлять правами других пользователей, управлять всеми элементами коллекции
    MODERATOR(2), // Модератор может управлять всеми элементами коллекции, но не может управлять правами пользователей
    USER(1), // Обычный пользователь может добавлять, обновлять и удалять свои элементы коллекции
    ABOBA(0); // Пользователь без прав, может только просматривать коллекцию

    private final int permissionLevel;
    PermissionType(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
    public int getPermissionLevel() {
        return permissionLevel;
    }

    public static String getAllPermissions() {
        return Arrays.stream(PermissionType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}