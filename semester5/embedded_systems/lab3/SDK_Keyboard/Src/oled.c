#include "oled.h"
#include "i2c.h"

static uint8_t OLED_Buffer[1024];

static OLED_t OLED;


static HAL_StatusTypeDef oled_WriteCommand(uint8_t command) {
	return HAL_I2C_Mem_Write(&hi2c1,OLED_I2C_ADDR,0x00,1,&command,1,10);
}


uint8_t oled_Init(void) {
  static const uint8_t commands[] = {0xAE, 0x20, 0x10, 0xB0, 0xC8, 0x00, 0x10, 0x40, 0x81, 0xFF, 0xA1, 0xA6, 0xA8, 0x3F, 0xA4, 0xD3, 0x00, 0xD5, 0xF0, 0xD9, 0x22, 0xDA, 0x12, 0xDB, 0x20, 0x8D, 0x14, 0xAF};
  HAL_Delay(100); /* Power-on settling, before the scheduler starts. */
  if (HAL_I2C_IsDeviceReady(&hi2c1, OLED_I2C_ADDR, 2, 10) != HAL_OK) return 0;
  for (unsigned i = 0; i < sizeof(commands); ++i)
    if (oled_WriteCommand(commands[i]) != HAL_OK) return 0;
  oled_Fill(Black);
  for (uint8_t page = 0; page < 8; ++page)
    if (oled_UpdatePage(page) != HAL_OK) return 0;
  OLED.CurrentX = OLED.CurrentY = 0;
  OLED.Initialized = 1;
  return 1;
}

void oled_Fill(OLED_COLOR color) {
	uint32_t i;

	for(i = 0; i < sizeof(OLED_Buffer); i++) {
		OLED_Buffer[i] = (color == Black) ? 0x00 : 0xFF;
	}
}


/* The caller holds the bus mutex for one page, then releases it. */
HAL_StatusTypeDef oled_UpdatePage(uint8_t page) {
  if (page >= 8) return HAL_ERROR;
  if (oled_WriteCommand(0xB0 + page) != HAL_OK ||
      oled_WriteCommand(0x00) != HAL_OK ||
      oled_WriteCommand(0x10) != HAL_OK) return HAL_ERROR;
  return HAL_I2C_Mem_Write(&hi2c1, OLED_I2C_ADDR, 0x40, 1,
                           &OLED_Buffer[OLED_WIDTH * page], OLED_WIDTH, 10);
}

void oled_UpdateScreen(void) {
  for (uint8_t page = 0; page < 8; ++page) (void)oled_UpdatePage(page);
}

void oled_DrawPixel(uint8_t x, uint8_t y, OLED_COLOR color) {
	if (x >= OLED_WIDTH || y >= OLED_HEIGHT) {
		return;
	}

	if (OLED.Inverted) {
		color = (OLED_COLOR)!color;
	}

	if (color == White) {
		OLED_Buffer[x + (y / 8) * OLED_WIDTH] |= 1 << (y % 8);
	} else {
		OLED_Buffer[x + (y / 8) * OLED_WIDTH] &= ~(1 << (y % 8));
	}
}

void oled_DrawHLine(uint8_t x1, uint8_t x2, uint8_t y, OLED_COLOR color) {
	for(int i = x1; i <= x2; i++) {
		oled_DrawPixel(i, y, color);
	}
}
void oled_DrawVLine(uint8_t y1, uint8_t y2, uint8_t x, OLED_COLOR color) {
	for(int i = y1; i <= y2; i++) {
		oled_DrawPixel(x, i, color);
	}
}
void oled_DrawSquare(uint8_t x1, uint8_t x2, uint8_t y1, uint8_t y2, OLED_COLOR color) {
	oled_DrawHLine(x1, x2, y1, color);
	oled_DrawHLine(x1, x2, y2, color);
	oled_DrawVLine(y1, y2, x1, color);
	oled_DrawVLine(y1, y2, x2, color);
}
char oled_WriteChar(char ch, FontDef Font, OLED_COLOR color) {
	uint32_t i, b, j;

	if (OLED_WIDTH <= (OLED.CurrentX + Font.FontWidth) ||
			OLED_HEIGHT <= (OLED.CurrentY + Font.FontHeight)) {
		return 0;
	}

	for (i = 0; i < Font.FontHeight; i++) {
		b = Font.data[(ch - 32) * Font.FontHeight + i];
		for (j = 0; j < Font.FontWidth; j++) {
			if ((b << j) & 0x8000) {
				oled_DrawPixel(OLED.CurrentX + j, (OLED.CurrentY + i), (OLED_COLOR) color);
			} else {
				oled_DrawPixel(OLED.CurrentX + j, (OLED.CurrentY + i), (OLED_COLOR)!color);
			}
		}
	}

	OLED.CurrentX += Font.FontWidth;

	return ch;
}


char oled_WriteString(char* str, FontDef Font, OLED_COLOR color) {
	while (*str) {
		if (oled_WriteChar(*str, Font, color) != *str) {
			return *str;
		}
		str++;
	}
	return *str;
}

void oled_SetCursor(uint8_t x, uint8_t y) {
	OLED.CurrentX = x;
	OLED.CurrentY = y;
}
