package utility;

/**
 * Интерфейс для объектов, представляющих консоль для ввода и вывода данных.
 */
public interface Console {
    /**
     * Выводит объект в стандартный поток вывода.
     * @param obj объект для вывода
     */
    void print(Object obj);

    /**
     * Выводит объект в стандартный поток вывода с переводом строки.
     * @param obj объект для вывода
     */
    void println(Object obj);

    /**
     * Выводит объект в стандартный поток ошибок.
     * @param obj объект для вывода
     */
    void printError(Object obj);

    /**
     * Выводит два объекта в виде таблицы.
     * @param obj1 первый объект
     * @param obj2 второй объект
     */
    void printTable(Object obj1, Object obj2);

    /**
     * Считывает строку из стандартного потока ввода.
     * @return считанная строка
     */
    String readln();
}