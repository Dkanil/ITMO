package com.lab6.client.utility;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Класс для чтения данных из файла, реализующий интерфейс Console. Заглушает стандартный поток вывода.
 */
public class FileConsole implements Console {
    private final BufferedReader input;

    /**
     * Конструктор для создания FileConsole.
     * @param input Буферизированный ридер для чтения данных из файла.
     */
    public FileConsole(BufferedReader input) {
        this.input = input;
    }

    /**
     * Заглушает поток вывода.
     * @param obj Объект для печати.
     */
    @Override
    public void print(Object obj) {
    }

    /**
     * Заглушает поток вывода с новой строки.
     * @param obj Объект для печати.
     */
    @Override
    public void println(Object obj) {
    }

    /**
     * Печатает сообщение об ошибке в стандартный поток ошибок.
     * @param obj Сообщение об ошибке.
     */
    @Override
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    /**
     * Считывает строку из файла.
     * @return Считанная строка.
     * @throws IllegalArgumentException Если произошла ошибка ввода-вывода.
     */
    @Override
    public String readln() {
        try {
            return input.readLine();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
