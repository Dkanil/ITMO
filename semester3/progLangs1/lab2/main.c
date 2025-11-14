#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int x;
    int y;
} Point;

typedef enum {
    WTYPE,
    ATYPE,
    MTYPE
} UnitType;

typedef struct {
    char * name;
    Point position;
    int health;
    UnitType type;
} Unit;

typedef struct {
    Unit* units;
    int size;
} Array;

void Print(Array*);
void AddNewUnit(Array*, Unit*);
void RemoveUnit(Array*, int);

int main() {
    Array * a = (Array*)malloc(sizeof(Array));
    if (a == NULL) {
        printf("Error while trying to allocate memory");
        return 1;
    }
    a->size = 0;
    a->units = NULL;
    Unit unit1 = {"aboba", {0, 0}, 1, WTYPE};
    Unit unit2 = {"baobab", {2, 10}, 15, ATYPE};
    AddNewUnit(a, &unit1);
    AddNewUnit(a, &unit2);
    Print(a);
    printf("\n");

    RemoveUnit(a, 0);
    Print(a);
    free(a->units);
    free(a);
    a = NULL;
}

void AddNewUnit(Array* a, Unit* u) {
    Unit *buf = realloc(a->units, (a->size + 1) * sizeof(Unit));
    if (buf != NULL) {
        a->units = buf;
        a->units[a->size] = *u;
        a->size++;
    } else {
        printf("Error while trying to allocate memory");
    }
}

void RemoveUnit(Array* a, int index) {
    if (index < 0 || index >= a->size) {
        printf("Index out of bounds\n");
        return;
    }
    if (a->units == NULL) {
        printf("Array is empty\n");
        return;
    }
    for (int i = 0; i < a->size; ++i) {
        if (i > index) {
            a->units[i-1] = a->units[i];
        }
    }
    a->size--;
    Unit *buf = realloc(a->units, a->size * sizeof(Unit));
    if (buf != NULL) {
        a->units = buf;
    } else {
        printf("Error while trying to allocate memory");
    }
}

void Print(Array* a) {
    for (int i = 0; i < a->size; ++i) {
        printf("a[%d]: { name = \"%s\", position = {x = %d, y = %d}, heatlh = %d, type = %d\n", i, a->units[i].name,
               a->units[i].position.x, a->units[i].position.y, a->units[i].health, a->units[i].type);
    }
}