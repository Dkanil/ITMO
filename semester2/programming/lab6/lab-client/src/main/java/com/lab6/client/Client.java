package com.lab6.client;

import com.lab6.common.utility.Console;
import com.lab6.common.utility.Request;
import com.lab6.common.utility.StandartConsole;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//Вариант 88347
public final class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        var console = new StandartConsole();
        while (true) {
             try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                console.println("Введите команду:");
                String inputCommand = console.readln();
                Request request = new Request(inputCommand, null);
                out.writeObject(request);

                // Получение и обработка ответа
                Request response = (Request) in.readObject();
                console.println(response.getCommandString());


        } catch (IOException | ClassNotFoundException e) {
            console.printError("Ошибка при работе с сервером: " + e.getMessage());
        }            }
    }
}