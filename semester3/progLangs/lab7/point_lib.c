#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct Point {
    int x;
    int y;
} Point;

typedef int (*MyFilter)(Point p);

int filter_array(Point * points, int len, Point * res, MyFilter filter_point){
    int final_res_len = 0;
    for (int i = 0; i < len; i++) {
        if (filter_point(points[i])) {
            res[final_res_len] = points[i];
            final_res_len++;
        }
    }
    return final_res_len;
}
