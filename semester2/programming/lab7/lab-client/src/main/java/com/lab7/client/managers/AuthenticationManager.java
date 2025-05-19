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

    public static Pair<String, String> sendAuthenticationRequest(NetworkManager networkManager, Console console, Pair<String, String> user, String inputCommand) throws IOException, ClassNotFoundException {
        Request request = new Request(inputCommand, user);
        networkManager.send(request);
        Response authResponse = networkManager.receive();
        if (authResponse.getExecutionStatus().isSuccess()) {
            console.println(authResponse.getExecutionStatus().getMessage());
            return user;
        } else {
            console.printError(authResponse.getExecutionStatus().getMessage());
            return null;
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
                Pair<String, String> user = sendAuthenticationRequest(networkManager, console, new Pair<>(username, password), inputCommand);
                if (user != null) {
                    return user;
                }
            } else {
                console.printError("Команда '" + inputCommand + "' не найдена!");
            }
        }
    }
}
