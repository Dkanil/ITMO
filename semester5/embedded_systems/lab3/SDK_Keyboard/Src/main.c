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
#include "FreeRTOS.h"
#include "task.h"
#include "queue.h"
#include "semphr.h"
#include "kb.h"
#include "sdk_uart.h"
#include "pca9538.h"
#include "oled.h"
#include "fonts.h"
#include "audio_clip.h"
#include "audio_adpcm.h"
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
static void Pong_InitRTOS(void);
static void Audio_Init(void);
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
  if (!oled_Init() || Set_Keyboard() != HAL_OK) Error_Handler();
  Audio_Init();
  Pong_InitRTOS();

  /* USER CODE END 2 */
 
 

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {

    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
      /* The FreeRTOS scheduler owns execution after Pong_InitRTOS(). */

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
/* PONG CORE BEGIN: hardware-independent rules, coordinates in 1/512 pixel (ball speed halved). */
#define KEY_1    (1u << 0)
#define KEY_2    (1u << 1)
#define KEY_3    (1u << 2)
#define KEY_5    (1u << 4)
#define KEY_7    (1u << 6)
#define KEY_0    (1u << 10)
#define KEY_9    (1u << 8)
#define Q 512
#define FIELD_TOP 14
#define FIELD_BOTTOM 62
#define PADDLE_HEIGHT 14
#define WIN_SCORE 7
#define GAME_PERIOD_MS 10

typedef enum { MENU, COUNTDOWN, PLAYING, PAUSED, FINISHED } GameMode;
typedef struct {
  GameMode mode;
  int32_t left_y, right_y, ball_x, ball_y, vx, vy;
  uint32_t deadline;
  uint16_t rally, best_rally;
  uint8_t left_score, right_score, winner, serve_id, countdown;
  int8_t serve_direction;
} Pong;

static int32_t Clamp(int32_t value, int32_t low, int32_t high)
{
  return value < low ? low : value > high ? high : value;
}

static void Pong_Serve(Pong *g, uint32_t now, uint32_t delay)
{
  g->ball_x = 64 * Q;
  g->ball_y = 38 * Q;
  g->vx = g->serve_direction * 230;
  g->vy = (g->serve_id++ & 1u) ? 115 : -115;
  g->rally = 0;
  g->deadline = now + delay;
  g->countdown = (uint8_t)((delay + 999u) / 1000u);
  g->mode = COUNTDOWN;
}

static void Pong_Init(Pong *g)
{
  memset(g, 0, sizeof(*g));
  g->left_y = g->right_y = 31 * Q;
  g->ball_x = 64 * Q;
  g->ball_y = 38 * Q;
  g->serve_direction = 1;
  g->mode = MENU;
}

static void Pong_Start(Pong *g, uint32_t now)
{
  uint16_t best = g->best_rally;
  Pong_Init(g);
  g->best_rally = best;
  Pong_Serve(g, now, 3000);
}

static void Pong_MovePaddles(Pong *g, uint16_t keys)
{
  int32_t step = 840; /* 164 pixels/second, independent of OLED refresh. */
  int left = !!(keys & KEY_7) - !!(keys & KEY_1);
  int right = !!(keys & KEY_9) - !!(keys & KEY_3);
  g->left_y = Clamp(g->left_y + left * step, FIELD_TOP * Q,
                     (FIELD_BOTTOM - PADDLE_HEIGHT + 1) * Q);
  g->right_y = Clamp(g->right_y + right * step, FIELD_TOP * Q,
                      (FIELD_BOTTOM - PADDLE_HEIGHT + 1) * Q);
}

static void Pong_Bounce(Pong *g, int32_t paddle_y, int direction)
{
  int32_t speed = g->vx < 0 ? -g->vx : g->vx;
  int32_t offset = g->ball_y - (paddle_y + (PADDLE_HEIGHT / 2) * Q);
  speed = Clamp(speed + 22, 230, 650); /* Faster on every return, with a cap. */
  g->vx = direction * speed;
  g->vy = Clamp(offset * speed / (8 * Q), -500, 500);
  if (g->vy > -65 && g->vy < 65) g->vy = offset < 0 ? -65 : 65;
  ++g->rally;
  if (g->rally > g->best_rally) g->best_rally = g->rally;
}

/* Returns the player who scored (0 = no goal). */
static uint8_t Pong_Physics(Pong *g, uint32_t now)
{
  int32_t old_x = g->ball_x, old_y = g->ball_y;
  int32_t next_x = old_x + g->vx, next_y = old_y + g->vy;
  int32_t top = (FIELD_TOP + 1) * Q, bottom = (FIELD_BOTTOM - 1) * Q;
  if (next_y < top) { next_y = 2 * top - next_y; g->vy = -g->vy; }
  if (next_y > bottom) { next_y = 2 * bottom - next_y; g->vy = -g->vy; }
  g->ball_y = next_y;

  /* Test the crossed paddle plane; interpolate Y to prevent tunnelling. */
  int32_t plane = g->vx < 0 ? 7 * Q : 120 * Q;
  if ((g->vx < 0 && old_x >= plane && next_x <= plane) ||
      (g->vx > 0 && old_x <= plane && next_x >= plane)) {
    int32_t hit_y = old_y + (next_y - old_y) * (plane - old_x) / (next_x - old_x);
    int32_t paddle = g->vx < 0 ? g->left_y : g->right_y;
    if (hit_y + Q >= paddle && hit_y - Q <= paddle + (PADDLE_HEIGHT - 1) * Q) {
      int direction = g->vx < 0 ? 1 : -1;
      g->ball_y = hit_y;
      Pong_Bounce(g, paddle, direction);
      next_x = plane + direction * Q;
    }
  }
  g->ball_x = next_x;
  uint8_t scorer = 0;
  if (next_x < -Q) { ++g->right_score; scorer = 2; g->serve_direction = -1; }
  if (next_x > 128 * Q) { ++g->left_score; scorer = 1; g->serve_direction = 1; }
  if (scorer) {
    if (g->left_score >= WIN_SCORE || g->right_score >= WIN_SCORE) {
      g->winner = scorer;
      g->mode = FINISHED;
    } else {
      Pong_Serve(g, now, 1500);
    }
  }
  return scorer;
}

static uint8_t Pong_Tick(Pong *g, uint16_t held, uint16_t pressed, uint32_t now)
{
  if (pressed & KEY_5) { g->mode = MENU; return 0; }
  if (g->mode == MENU || g->mode == FINISHED) {
    if (pressed & KEY_2) Pong_Start(g, now);
    return 0;
  }
  if (pressed & KEY_0) {
    if (g->mode == PLAYING) g->mode = PAUSED;
    else if (g->mode == PAUSED) g->mode = PLAYING;
    return 0;
  }
  if (g->mode == PAUSED) return 0;
  Pong_MovePaddles(g, held);
  if (g->mode == COUNTDOWN) {
    if ((int32_t)(now - g->deadline) >= 0) { g->mode = PLAYING; g->countdown = 0; }
    else g->countdown = (uint8_t)((g->deadline - now + 999u) / 1000u);
    return 0;
  }
  return Pong_Physics(g, now);
}
/* PONG CORE END */

/* AUDIO PLAYER BEGIN: 8 kHz IMA ADPCM in Flash, 40 kHz PWM on PA5.
 * TIM6 updates one sample per interrupt. No busy wait or audio RAM buffer.
 * TIM2_CH1/PA5 is the sound output used by the SDK_Buzzer example.
 */
#define AUDIO_PWM_RATE 40000u
static ADPCMState audio_decoder;
static uint32_t audio_pwm_period;
static volatile uint32_t audio_sample_pos, audio_playing, audio_completed;

/* May run from the audio interrupt; it never calls the FreeRTOS API. */
static void Audio_Disable(void)
{
  TIM6->DIER = 0;
  TIM6->CR1 = 0;
  TIM6->SR = 0;
  TIM2->CCER = 0;
  TIM2->CR1 &= ~TIM_CR1_CEN;
  TIM2->CCR1 = 0;
  audio_playing = 0;
}

static void Audio_Init(void)
{
  __HAL_RCC_GPIOA_CLK_ENABLE();
  __HAL_RCC_TIM2_CLK_ENABLE();
  __HAL_RCC_TIM6_CLK_ENABLE();
  GPIO_InitTypeDef pin = {0};
  pin.Pin = GPIO_PIN_5;
  pin.Mode = GPIO_MODE_AF_PP;
  pin.Pull = GPIO_NOPULL;
  pin.Speed = GPIO_SPEED_FREQ_HIGH;
  pin.Alternate = GPIO_AF1_TIM2;
  HAL_GPIO_Init(GPIOA, &pin);

  uint32_t timer_clock = HAL_RCC_GetPCLK1Freq();
  if ((RCC->CFGR & RCC_CFGR_PPRE1) != 0) timer_clock *= 2;
  audio_pwm_period = timer_clock / AUDIO_PWM_RATE;
  if (audio_pwm_period < 2 || timer_clock % AUDIO_SAMPLE_RATE != 0 ||
      timer_clock / AUDIO_SAMPLE_RATE > 65536) Error_Handler();
  TIM2->CR1 = TIM_CR1_ARPE;
  TIM2->PSC = 0;
  TIM2->ARR = audio_pwm_period - 1;
  TIM2->CCR1 = 0;
  TIM2->CCMR1 = TIM_CCMR1_OC1M_1 | TIM_CCMR1_OC1M_2 | TIM_CCMR1_OC1PE;
  TIM2->CCER = 0;
  TIM2->EGR = TIM_EGR_UG;
  TIM2->SR = 0;

  TIM6->CR1 = 0;
  TIM6->PSC = 0;
  TIM6->ARR = timer_clock / AUDIO_SAMPLE_RATE - 1;
  TIM6->DIER = 0;
  TIM6->EGR = TIM_EGR_UG;
  TIM6->SR = 0;
  /* Lower urgency than the FreeRTOS interrupt ceiling, above SysTick. */
  HAL_NVIC_SetPriority(TIM6_DAC_IRQn, 6, 0);
  HAL_NVIC_EnableIRQ(TIM6_DAC_IRQn);
}

static void Audio_Stop(void)
{
  taskENTER_CRITICAL();
  Audio_Disable();
  taskEXIT_CRITICAL();
}

static void Audio_Start(void)
{
  taskENTER_CRITICAL();
  Audio_Disable();
  audio_decoder.predictor = audio_decoder.index = 0;
  audio_sample_pos = 0;
  TIM2->CNT = 0;
  TIM2->CCR1 = audio_pwm_period / 2;
  TIM2->EGR = TIM_EGR_UG;
  TIM2->SR = 0;
  TIM2->CCER = TIM_CCER_CC1E;
  TIM2->CR1 |= TIM_CR1_CEN;
  TIM6->CNT = 0;
  TIM6->EGR = TIM_EGR_UG;
  TIM6->SR = 0;
  audio_playing = 1;
  TIM6->DIER = TIM_DIER_UIE;
  TIM6->CR1 = TIM_CR1_CEN;
  taskEXIT_CRITICAL();
}

void TIM6_DAC_IRQHandler(void)
{
  if (!(TIM6->SR & TIM_SR_UIF)) return;
  TIM6->SR = 0;
  if (!audio_playing) return;
  uint32_t position = audio_sample_pos;
  if (position >= AUDIO_SAMPLE_COUNT) {
    Audio_Disable();
    ++audio_completed;
    return;
  }
  uint8_t packed = audio_clip[position / 2];
  uint8_t code = (position & 1u) ? packed >> 4 : packed & 15u;
  /* Deliberate hard clipping for a loud, distorted overdrive effect. */
  int32_t pcm = (int32_t)ADPCM_Decode(&audio_decoder, code) * 8;
  if (pcm > 32767) pcm = 32767;
  if (pcm < -32768) pcm = -32768;
  TIM2->CCR1 = ((uint32_t)(pcm + 32768) * audio_pwm_period) >> 16;
  audio_sample_pos = position + 1;
}

/* Only a transition to FINISHED starts the clip, once per completed match. */
static void Audio_OnModeChange(GameMode before, GameMode after)
{
  if (before == after) return;
  if (after == FINISHED) Audio_Start();
  else if (before == FINISHED || after == MENU) Audio_Stop();
}
/* AUDIO PLAYER END */

/* Each task owns its data. Only copies pass through the queues. */
typedef struct { uint16_t held; uint32_t sampled_at; } InputState;
static QueueHandle_t input_queue, frame_queue, log_queue;
static SemaphoreHandle_t i2c_mutex;
static TaskHandle_t keyboard_task, game_task, display_task;
static volatile uint32_t keyboard_scans, game_ticks, display_frames, i2c_errors;

static void Pong_Log(const char *message)
{
  char copy[64] = {0};
  snprintf(copy, sizeof(copy), "%s", message);
  (void)xQueueSend(log_queue, copy, 0);
}

/* Scan all 12 keys as a bit mask: both players can hold keys at once.
 * Only the active row is an output driven low; other rows are inputs.
 * Three or more keys can produce ghosting on a matrix without diodes. */
static uint16_t Pong_ReadKeys(void)
{
  static const uint8_t rows[4] = {ROW1, ROW2, ROW3, ROW4};
  uint16_t result = 0;
  uint8_t idle = 0xFF;
  uint8_t failed = 0, count = 0;
  xSemaphoreTake(i2c_mutex, portMAX_DELAY);
  for (uint8_t row = 0; row < 4; ++row) {
    uint8_t config = rows[row], input = 0x70;
    if (PCA9538_Write_Register(0xE2, CONFIG, &config) != HAL_OK) {
      failed = 1; break;
    }
    vTaskDelay(pdMS_TO_TICKS(1)); /* Matrix settling, yields the CPU. */
    if (PCA9538_Read_Inputs(0xE2, &input) != HAL_OK) { failed = 1; break; }
    for (uint8_t column = 0; column < 3; ++column) {
      if (!(input & (0x10u << column))) {
        result |= 1u << (row * 3 + column);
        ++count;
      }
    }
  }
  if (PCA9538_Write_Register(0xE2, CONFIG, &idle) != HAL_OK) failed = 1;
  xSemaphoreGive(i2c_mutex);
  if (failed) { ++i2c_errors; return 0; }
  return count > 2 ? 0 : result;
}

static void KeyboardTask(void *argument)
{
  (void)argument;
  TickType_t wake = xTaskGetTickCount();
  uint16_t stable = 0, candidate = 0;
  uint32_t changed[12] = {0};
  for (;;) {
    uint16_t raw = Pong_ReadKeys();
    uint32_t now = HAL_GetTick();
    for (uint8_t key = 0; key < 12; ++key) {
      uint16_t bit = 1u << key;
      if ((raw ^ candidate) & bit) { candidate ^= bit; changed[key] = now; }
      if (now - changed[key] >= 20) {
        stable = (stable & ~bit) | (candidate & bit);
      }
    }
    InputState state = { stable, now };
    xQueueOverwrite(input_queue, &state);
    ++keyboard_scans;
    vTaskDelayUntil(&wake, pdMS_TO_TICKS(10));
  }
}

static void GameTask(void *argument)
{
  (void)argument;
  Pong game;
  Pong_Init(&game);
  TickType_t wake = xTaskGetTickCount();
  uint16_t previous = 0;
  for (;;) {
    InputState input = {0};
    uint32_t now = HAL_GetTick();
    (void)xQueuePeek(input_queue, &input, 0);
    if (now - input.sampled_at > 100) input.held = 0;
    uint16_t pressed = input.held & ~previous;
    previous = input.held;
    GameMode old_mode = game.mode;
    uint8_t goal = Pong_Tick(&game, input.held, pressed, now);
    Audio_OnModeChange(old_mode, game.mode);
    if (goal) {
      char line[64];
      snprintf(line, sizeof(line), "P%u scores! %u:%u; best rally %u\r\n",
               goal, game.left_score, game.right_score, game.best_rally);
      Pong_Log(line);
    }
    if (old_mode != game.mode) {
      static const char * const modes[] = {"MENU", "COUNTDOWN", "PLAYING", "PAUSED", "FINISHED"};
      Pong_Log(modes[game.mode]);
    }
    xQueueOverwrite(frame_queue, &game);
    ++game_ticks;
    vTaskDelayUntil(&wake, pdMS_TO_TICKS(GAME_PERIOD_MS));
  }
}

static void Text(uint8_t x, uint8_t y, const char *text)
{
  oled_SetCursor(x, y);
  oled_WriteString((char *)text, Font_7x10, White);
}

static void Center(uint8_t y, const char *text)
{
  size_t width = strlen(text) * 7;
  Text(width < OLED_WIDTH ? (OLED_WIDTH - width) / 2 : 0, y, text);
}

static void FillRect(int x, int y, int width, int height)
{
  for (int yy = y; yy < y + height; ++yy)
    for (int xx = x; xx < x + width; ++xx)
      if (xx >= 0 && yy >= 0 && xx < OLED_WIDTH && yy < OLED_HEIGHT)
        oled_DrawPixel((uint8_t)xx, (uint8_t)yy, White);
}

static void Pong_Draw(const Pong *g)
{
  char line[19];
  oled_Fill(Black);
  if (g->mode == MENU) {
    Center(0, "PONG - FIRST TO 7");
    Text(0, 11, "P1 1:UP 7:DOWN");
    Text(0, 22, "P2 3:UP 9:DOWN");
    Text(0, 33, "2:START 0:PAUSE");
    snprintf(line, sizeof(line), "5:EXIT  BEST:%u", g->best_rally);
    Text(0, 44, line);
    Center(54, "HOLD KEYS TO MOVE");
    return;
  }
  if (g->mode == FINISHED) {
    Center(0, "MATCH COMPLETE!");
    snprintf(line, sizeof(line), "PLAYER %u WINS", g->winner);
    Center(13, line);
    snprintf(line, sizeof(line), "%u : %u", g->left_score, g->right_score);
    Center(25, line);
    snprintf(line, sizeof(line), "BEST RALLY: %u", g->best_rally);
    Center(37, line);
    Center(51, "2:REMATCH 5:MENU");
    return;
  }
  snprintf(line, sizeof(line), "P1 %u", g->left_score);
  Text(0, 0, line);
  snprintf(line, sizeof(line), "%u P2", g->right_score);
  Text(93, 0, line);
  Text(50, 0, "PONG");
  oled_DrawHLine(0, 127, 12, White);
  oled_DrawHLine(0, 127, 63, White);
  for (uint8_t y = FIELD_TOP; y < FIELD_BOTTOM; y += 6) FillRect(63, y, 1, 3);
  FillRect(3, g->left_y / Q, 3, PADDLE_HEIGHT);
  FillRect(122, g->right_y / Q, 3, PADDLE_HEIGHT);
  if (g->mode == COUNTDOWN) {
    FillRect(37, 24, 54, 24);
    oled_SetCursor(43, 26);
    oled_WriteString("READY", Font_7x10, Black);
    snprintf(line, sizeof(line), "%u", g->countdown);
    oled_SetCursor(60, 37);
    oled_WriteString(line, Font_7x10, Black);
  } else if (g->mode == PAUSED) {
    FillRect(22, 25, 84, 24);
    oled_SetCursor(43, 26);
    oled_WriteString("PAUSE", Font_7x10, Black);
    oled_SetCursor(29, 37);
    oled_WriteString("0:CONTINUE", Font_7x10, Black);
  } else {
    FillRect(g->ball_x / Q - 1, g->ball_y / Q - 1, 3, 3);
  }
}

static void DisplayTask(void *argument)
{
  (void)argument;
  Pong frame;
  Pong_Init(&frame);
  TickType_t wake = xTaskGetTickCount();
  for (;;) {
    (void)xQueuePeek(frame_queue, &frame, 0);
    Pong_Draw(&frame);
    /* Release the I2C mutex between OLED pages, so keyboard scans can run. */
    for (uint8_t page = 0; page < 8; ++page) {
      xSemaphoreTake(i2c_mutex, portMAX_DELAY);
      if (oled_UpdatePage(page) != HAL_OK) ++i2c_errors;
      xSemaphoreGive(i2c_mutex);
    }
    ++display_frames;
    vTaskDelayUntil(&wake, pdMS_TO_TICKS(40));
  }
}

static void LogTask(void *argument)
{
  (void)argument;
  char line[64];
  for (;;) {
    if (xQueueReceive(log_queue, line, portMAX_DELAY) == pdPASS) {
      HAL_UART_Transmit(&huart6, (uint8_t *)line, strlen(line), 20);
      HAL_UART_Transmit(&huart6, (uint8_t *)"\r\n", 2, 20);
    }
  }
}

static void Pong_InitRTOS(void)
{
  input_queue = xQueueCreate(1, sizeof(InputState));
  frame_queue = xQueueCreate(1, sizeof(Pong));
  log_queue = xQueueCreate(8, 64);
  i2c_mutex = xSemaphoreCreateMutex();
  if (!input_queue || !frame_queue || !log_queue || !i2c_mutex) Error_Handler();
  if (xTaskCreate(KeyboardTask, "Keyboard", 384, NULL, 3, &keyboard_task) != pdPASS ||
      xTaskCreate(GameTask, "Pong", 512, NULL, 2, &game_task) != pdPASS ||
      xTaskCreate(DisplayTask, "OLED", 512, NULL, 1, &display_task) != pdPASS ||
      xTaskCreate(LogTask, "UART", 256, NULL, 1, NULL) != pdPASS) Error_Handler();
  Pong_Log("PONG FreeRTOS: 1/7=P1 3/9=P2 2=start 0=pause 5=menu");
  vTaskStartScheduler();
  Error_Handler();
}

void vApplicationMallocFailedHook(void) { Error_Handler(); }
void vApplicationStackOverflowHook(TaskHandle_t task, char *name)
{
  (void)task; (void)name;
  Error_Handler();
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
