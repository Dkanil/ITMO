#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct Point {
    int x;
    int y;
} Point;

// 5. Функция на C для каждой пары считаем расстояние и возвращаем массив с результатами
double process_point(Point * p1, Point * p2) {
    int dx = p2->x - p1->x;
    int dy = p2->y - p1->y;
    return sqrt((double)dx * dx + dy * dy);
}

// Функция обработки массива точек
void process(Point* p, int len, double * out_results) {
    for (int i = 0; i < len; i += 2) {
        out_results[i / 2] = process_point(&p[i], &p[i + 1]);
    }
}

