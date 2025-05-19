package com.lab7.client.managers;

import com.lab7.client.utility.Console;
import com.lab7.common.utility.Request;
import com.lab7.common.utility.Pair;
import com.lab7.common.utility.Response;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationManager {
    private static String getHash(String password) {
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

    public static Pair<String, String> authenticateUser(NetworkManager networkManager, Console console) throws IOException, ClassNotFoundException {
        while (true) {
            console.println("Введите команду 'register' для регистрации или 'login' для авторизации:");
            String inputCommand = console.readln().trim().toLowerCase();
            if (inputCommand.equals("register") || inputCommand.equals("login")) {

                console.println("Введите логин:");
                String username = console.readln();
                console.println("Введите пароль для авторизации:");
                String password = getHash(console.readln());
                Request request = new Request(inputCommand, new Pair<>(username, password));

                networkManager.send(request);
                Response authResponse = networkManager.receive();
                if (authResponse.getExecutionStatus().isSuccess()) {
                    console.println(authResponse.getExecutionStatus().getMessage());
                    return new Pair<>(username, password);
                } else {
                    console.printError(authResponse.getExecutionStatus().getMessage());
                }
            } else {
                console.printError("Команда '" + inputCommand + "' не найдена!");
            }
        }
    }
}
