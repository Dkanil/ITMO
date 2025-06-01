package com.lab8.server.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String getHash(String password) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] hashedPassword = md.digest();

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedPassword) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Ошибка: алгоритм шифрования SHA-1 не поддерживается.");
            throw new RuntimeException(e);
        }
    }
}
