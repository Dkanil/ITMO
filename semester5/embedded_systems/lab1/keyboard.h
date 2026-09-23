#ifndef KEYBOARD_H
#define KEYBOARD_H

#include <stdio.h>
#include <stdint.h>
#include <string.h>

#include "main.h"

void initKeyboard();
char readKey();
char scanKeyboard();

extern char lastKey;
extern uint32_t lastScanTime;

#endif