package utility;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;

import java.io.InputStreamReader;
import java.io.FileWriter;

public class StandartConsole implements Console {
    public void print(Object obj) {
        System.out.print(obj);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public void printError(Object obj) {
        System.err.println("Error:" + obj);
    }

    public void printTable(Object obj1, Object obj2) {
        System.out.printf("%-20s: %s\n", obj1, obj2);
    }

    public String readln() {
        return System.console().readLine();
    }
}
