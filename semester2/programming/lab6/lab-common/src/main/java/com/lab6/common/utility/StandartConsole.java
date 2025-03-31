package com.lab6.common.utility;

/**
 * Класс, представляющий стандартную консоль для ввода и вывода данных.
 */
public class StandartConsole implements Console {

    /**
     * Выводит объект в стандартный поток вывода.
     * @param obj объект для вывода
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит объект в стандартный поток вывода с переводом строки.
     * @param obj объект для вывода
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Выводит объект в стандартный поток ошибок.
     * @param obj объект для вывода
     */
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    /**
     * Выводит два объекта в виде таблицы.
     * @param obj1 первый объект
     * @param obj2 второй объект
     */
    public void printTable(Object obj1, Object obj2) {
        System.out.printf("%-20s: %s\n", obj1, obj2);
    }

    /**
     * Считывает строку из стандартного потока ввода.
     * @return считанная строка
     */
    public String readln() {
        return new java.util.Scanner(System.in).nextLine();
    }
}
