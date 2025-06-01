package com.lab8.client.managers;

import com.lab8.client.utility.Console;
import com.lab8.common.utility.Request;
import com.lab8.common.utility.Pair;
import com.lab8.common.utility.Response;

import java.io.IOException;

public class AuthenticationManager {
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
                String password = console.readln();
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
