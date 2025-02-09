package utility;
// import java.util.Scanner;

public interface Console {
    void print(Object obj);
    void println(Object obj);
    void printError(Object obj);
    void printTable(Object obj1, Object obj2);
    String readln();
}