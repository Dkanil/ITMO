/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2019 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/* USER CODE END Header */

/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "i2c.h"
#include "usart.h"
#include "gpio.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include "kb.h"
#include "sdk_uart.h"
#include "pca9538.h"
#include "oled.h"
#include "fonts.h"
#include <stdint.h>
#include <limits.h>
#include <stdio.h>
#include <string.h>
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */

/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/

/* USER CODE BEGIN PV */

/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
/* USER CODE BEGIN PFP */
static void Calculator_Init(void);
static void Calculator_Poll(void);
/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */

  /* USER CODE END 1 */
  

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */

  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_I2C1_Init();
  MX_USART6_UART_Init();
  /* USER CODE BEGIN 2 */
  oled_Init();
  Calculator_Init();

  /* USER CODE END 2 */
 
 

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {

    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
	  Calculator_Poll();
	  HAL_Delay(5); /* Короткий опрос: клавиатура остаётся отзывчивой. */

  }
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  /** Configure the main internal regulator output voltage 
  */
  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);
  /** Initializes the CPU, AHB and APB busses clocks 
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSE;
  RCC_OscInitStruct.HSEState = RCC_HSE_ON;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
  RCC_OscInitStruct.PLL.PLLM = 25;
  RCC_OscInitStruct.PLL.PLLN = 336;
  RCC_OscInitStruct.PLL.PLLP = RCC_PLLP_DIV2;
  RCC_OscInitStruct.PLL.PLLQ = 4;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB busses clocks 
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV4;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV2;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_5) != HAL_OK)
  {
    Error_Handler();
  }
}

/* USER CODE BEGIN 4 */
/* CALCULATOR CORE BEGIN
 * Два операнда int32_t: от -2147483648 до 2147483647.
 * Промежуточные вычисления выполняем в int64_t, чтобы обнаружить
 * переполнение ДО преобразования результата к int32_t.
 */
/* Различаем переполнение и деление на ноль для сообщения на экране. */
enum {
  CALC_ERROR_NONE = 0,
  CALC_ERROR_OVERFLOW = 1,
  CALC_ERROR_DIV_ZERO = 2
};

typedef struct {
  int32_t left;
  int64_t magnitude;       /* Модуль набираемого числа (включая 2147483648). */
  uint8_t negative;
  uint8_t entered;
  uint8_t pending;         /* Уже выбрана операция, вводим второй операнд. */
  uint8_t result;
  uint8_t error;          /* Код ошибки из перечисления CALC_ERROR_*. */
  char operation;
} Calculator;

static Calculator calc;

static void Calculator_Clear(void)
{
  memset(&calc, 0, sizeof(calc));
  calc.operation = '+';
}

static int64_t Calculator_Value(void)
{
  return calc.negative ? -calc.magnitude : calc.magnitude;
}

static void Calculator_Digit(uint8_t digit)
{
  if (calc.error || calc.result) Calculator_Clear();
  int64_t next = calc.magnitude * 10 + digit;
  int64_t limit = calc.negative ? (int64_t)INT32_MAX + 1 : INT32_MAX;
  if (next > limit) {
    calc.error = CALC_ERROR_OVERFLOW;
    return;
  }
  calc.magnitude = next;
  calc.entered = 1;
}

static void Calculator_Sign(void)
{
  if (calc.error) return;
  /* -INT32_MIN не помещается в int32_t. */
  if (calc.negative && calc.magnitude > INT32_MAX) {
    calc.error = CALC_ERROR_OVERFLOW;
    return;
  }
  calc.negative = !calc.negative;
  if (calc.result) calc.left = (int32_t)Calculator_Value();
}

static void Calculator_Operation(void)
{
  if (calc.error) return;
  if (!calc.pending) {
    if (!calc.entered) return;
    calc.left = (int32_t)Calculator_Value();
    calc.magnitude = 0;
    calc.negative = 0;
    calc.entered = 0;
    calc.result = 0;
    calc.pending = 1;
    calc.operation = '+';
  } else {
    /* Повторные короткие нажатия *: сложение, вычитание,
     * умножение, целочисленное деление, снова сложение. */
    calc.operation = calc.operation == '+' ? '-' :
                     calc.operation == '-' ? '*' :
                     calc.operation == '*' ? '/' : '+';
  }
}

static void Calculator_Equals(void)
{
  if (calc.error || !calc.pending || !calc.entered) return;
  int64_t right = Calculator_Value();
  int64_t answer;
  switch (calc.operation) {
    case '+': answer = (int64_t)calc.left + right; break;
    case '-': answer = (int64_t)calc.left - right; break;
    case '*': answer = (int64_t)calc.left * right; break;
    case '/':
      /* Проверяем делитель ДО деления, чтобы не делить на ноль. */
      if (right == 0) {
        calc.error = CALC_ERROR_DIV_ZERO;
        return;
      }
      /* В C целочисленное деление отбрасывает дробную часть к нулю:
       * 7 / 3 = 2, -7 / 3 = -2. int64_t позволяет безопасно вычислить
       * INT32_MIN / -1; общая проверка ниже обнаружит переполнение. */
      answer = (int64_t)calc.left / right;
      break;
    default: return;
  }
  if (answer < INT32_MIN || answer > INT32_MAX) {
    calc.error = CALC_ERROR_OVERFLOW;
    return;
  }
  calc.left = (int32_t)answer;
  calc.negative = answer < 0;
  calc.magnitude = answer < 0 ? -answer : answer;
  calc.pending = 0;
  calc.result = 1;
  /* Результат можно использовать как первый операнд следующего расчёта. */
}
/* CALCULATOR CORE END */

static void Calculator_Line(uint8_t y, char *text)
{
  oled_SetCursor(0, y);
  oled_WriteString(text, Font_7x10, White);
}

static void Calculator_Draw(void)
{
  char line[19]; /* На дисплее помещается 18 символов шрифта 7x10. */
  oled_Fill(Black);
  Calculator_Line(0, "INTEGER CALC");
  if (calc.error) {
    Calculator_Line(12, calc.error == CALC_ERROR_DIV_ZERO ?
                    "DIVIDE BY ZERO" : "OVERFLOW");
    Calculator_Line(24, "Hold * to clear");
  } else if (calc.pending) {
    snprintf(line, sizeof(line), "A: %ld", (long)calc.left);
    Calculator_Line(12, line);
    snprintf(line, sizeof(line), "OP: %c", calc.operation);
    Calculator_Line(24, line);
    if (calc.entered)
      snprintf(line, sizeof(line), "B: %ld", (long)Calculator_Value());
    else
      snprintf(line, sizeof(line), "B: %s_", calc.negative ? "-" : "");
    Calculator_Line(36, line);
  } else {
    snprintf(line, sizeof(line), "%s%ld", calc.result ? "= " : "",
             (long)Calculator_Value());
    Calculator_Line(12, line);
    if (calc.negative && !calc.entered) Calculator_Line(12, "-_");
    Calculator_Line(24, "*: + - * /");
    Calculator_Line(36, "#: equals");
  }
  Calculator_Line(52, "Hold: * C  # +/-");
  oled_UpdateScreen();
}

/* Читаем всю матрицу. Неактивные строки оставляем входами,
 * активную строку тянем к нулю: нет конфликта выходов при двух кнопках.
 * ROW1 — верхний ряд, P4/P5/P6 — левый/средний/правый столбец.
 * Физическая раскладка: 1 2 3 / 4 5 6 / 7 8 9 / * 0 #.
 * 0 = нет кнопок, 0xFF = несколько кнопок или ошибка I2C.
 */
static uint8_t Calculator_ReadKey(void)
{
  static const uint8_t rows[4] = {ROW1, ROW2, ROW3, ROW4};
  static const uint8_t keys[4][3] = {{'1','2','3'}, {'4','5','6'},
                                  {'7','8','9'}, {'*','0','#'}};
  uint8_t found = 0, count = 0, input = 0x70;
  for (uint8_t row = 0; row < 4; ++row) {
    uint8_t config = rows[row];
    if (PCA9538_Write_Register(0xE2, CONFIG, &config) != HAL_OK)
      return 0xFF;
    /* Даём сигналам матрицы установиться после смены строки. */
    HAL_Delay(1);
    if (PCA9538_Read_Inputs(0xE2, &input) != HAL_OK) return 0xFF;
    for (uint8_t column = 0; column < 3; ++column) {
      if (!(input & (0x10u << column))) {
        found = keys[row][column];
        ++count;
      }
    }
  }
  uint8_t idle = 0xFF;
  if (PCA9538_Write_Register(0xE2, CONFIG, &idle) != HAL_OK) return 0xFF;
  return count > 1 ? 0xFF : found;
}

static void Calculator_Init(void)
{
  Calculator_Clear();
  if (Set_Keyboard() != HAL_OK) {
    oled_Fill(Black);
    Calculator_Line(12, "KEYBOARD I2C ERROR");
    oled_UpdateScreen();
    Error_Handler();
  }
  Calculator_Draw();
}

static void Calculator_Key(uint8_t key, uint8_t held)
{
  if (key >= '0' && key <= '9') Calculator_Digit(key - '0');
  else if (key == '*') {
    if (held) Calculator_Clear();
    else Calculator_Operation();
  } else if (key == '#') {
    if (held) Calculator_Sign();
    else Calculator_Equals();
  }
  Calculator_Draw();
}

static void Calculator_Poll(void)
{
  static uint8_t candidate, stable, pressed, long_done, blocked;
  static uint32_t changed_at, pressed_at;
  uint8_t raw = Calculator_ReadKey();
  uint32_t now = HAL_GetTick();
  if (raw == 0xFF) {
    /* После ошибки/нескольких кнопок ждём полного отпускания. */
    blocked = 1;
    pressed = 0;
  }
  if (raw != candidate) {
    candidate = raw;
    changed_at = now;
  }
  /* Изменение принимается только после 30 мс устойчивого состояния. */
  if (now - changed_at >= 30 && stable != candidate) {
    stable = candidate;
    if (!stable) {
      if (pressed && !long_done && !blocked &&
          (pressed == '*' || pressed == '#'))
        Calculator_Key(pressed, 0);
      pressed = 0;
      blocked = 0;
    } else if (!blocked) {
      if (pressed) {
        /* Смена клавиши без отпускания не должна порождать второй ввод. */
        blocked = 1;
        pressed = 0;
      } else {
        pressed = stable;
        pressed_at = now;
        long_done = 0;
        if (pressed >= '0' && pressed <= '9') Calculator_Key(pressed, 0);
      }
    }
  }
  /* Цифра вводится один раз при нажатии. У * и # короткое действие
   * выполняется при отпускании, длинное — через 700 мс удержания.
   * Поэтому очистка/смена знака не вызывают ещё и выбор операции/равно.
   */
  if (pressed && !blocked && !long_done && raw == pressed &&
      (pressed == '*' || pressed == '#') && now - pressed_at >= 700) {
    long_done = 1;
    Calculator_Key(pressed, 1);
  }
}
/* USER CODE END 4 */

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
  /* При ошибке инициализации продолжение работы небезопасно:
   * останавливаемся здесь; в отладчике видна точка отказа. */
  __disable_irq();
  while (1) { }

  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{ 
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     tex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
