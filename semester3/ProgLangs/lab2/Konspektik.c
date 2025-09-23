#include <stdio.h>
#include <stdlib.h>

void encrypt(int * p);

int not_main(int argc, char *argv[]) {
    /*
     * Указатели
     */
    int a = 1;
    int * b = &a; // b это int указатель на a
    // Разыменование указателя
    int c = * b; // c это int, равный значению по указателю b (то есть a)
    printf("a = %d, b = %p, c = %d\n", a, b, c);
    encrypt(&a); // передаем адрес a в функцию
    printf("a = %d, b = %p, c = %d\n", a, b, c);
    // Используют для передачи больших структур в функции, чтобы не копировать их

    /*
     * Работа с массивами через указатели
     */
    printf("argv = %s\n", argv[0]);
    char * word = argv[0];
    printf("res = %c\n", *word);
    printf("res = %c\n", word[0]);
    printf("res = %c\n", *(word + 1));
    printf("res = %c\n", word[1]); // 1[word] - так тоже работает, тк 1[word] == *(1 + word) == *(word + 1) == word[1]

    // Многомерные массивы
    // pointer(addr) => pointer(addr) => value
    int ** t; // t это указатель на указатель на int

    /*
     * Аллокация памяти - динамическое выделение памяти
     */
    int * arr;
    arr = (int*)malloc(10 * sizeof(int)); // выделяет память под 10 int (Важно: не инициализирует память)
    if (arr == NULL) {
        return 1;
    }

    for (int i = 0; i < 10; ++i) {
        arr[i] = i;
    }
    for (int i = 0; i < 10; ++i) {
        printf("arr[%d] = %d\n", i, arr[i]);
    }

    int * temp;
    temp = (int*)realloc(arr, 11 * sizeof(int));
    if (temp != NULL) {
        arr = temp;
    }
    free(arr); // освобождение памяти
    arr = NULL; // чтобы не было висячих указателей (указатель, который указывает на уже освобожденную память)

    int * ca = calloc(1, sizeof(int)); // выделяет память и заполняет нулями
    for (size_t i = 0; i < 1; i++) {
        ca[i] = 4444;
    }

    /*
     * Обязательная часть - освобождение памяти
     */
    free(ca);
}

void encrypt(int * f) {
    // f это int указатель на a
    *f = 4; // разыменовываем f и увеличиваем значение на 1
}
