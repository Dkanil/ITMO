#include "main.h"
#include "tm1637.h"
#include "keyboard.h"

volatile uint32_t tickCount;
uint32_t last_display_update;
uint16_t counter;
char lastKey;
uint32_t lastScanTime;


void osSystickHandler(void) {
  tickCount++;
}

void initGPIO() {
  // Включаем тактирование GPIOA и GPIOB
  RCC->AHBENR |= RCC_AHBENR_GPIOAEN | RCC_AHBENR_GPIOBEN;

  // Настройка PA5 (бортовой), PA9 (зеленый LED), PA15 (красный LED) как выходы
  GPIOA->MODER &= ~((3U << (5 * 2)) | (3U << (9 * 2)) | (3U << (15 * 2)));
  GPIOA->MODER |=  (1U << (5 * 2)) | (1U << (9 * 2)) | (1U << (15 * 2));

  GPIOA->OTYPER &= ~((1 << 5) | (1 << 9) | (1 << 15));
  GPIOA->BRR = (1 << 5) | (1 << 9) | (1 << 15); // Выключаем все светодиоды
}

void initUSART2() {
  // Включаем тактирование USART2
  RCC->APB1ENR |= RCC_APB1ENR_USART2EN;

  // Настраиваем PA2 и PA3 в альтернативный режим
  GPIOA->MODER = (GPIOA->MODER & ~(0xF << 4)) | (0xA << 4);
  GPIOA->AFR[0] = (GPIOA->AFR[0] & ~(0xFF << 8)) | (1 << 8) | (1 << 12);

  // Настраиваем USART2
  USART2->BRR = 417; // 48MHz/115200
  USART2->CR1 = USART_CR1_TE | USART_CR1_UE;
}

void initSysTick() {
  SysTick->LOAD = 47999; // 1ms при 48MHz
  SysTick->VAL = 0;
  SysTick->CTRL = (1 << 2) | (1 << 1) | (1 << 0);
}

int _write(int file, uint8_t *ptr, int len) {
  for (int i = 0; i < len; i++) {
    while (!(USART2->ISR & USART_ISR_TXE));
    USART2->TDR = ptr[i];
  }
  return len;
}

void checkTickCount() {
  if ((tickCount % 2000) == 0) {
    GPIOA->ODR ^= (1 << 5); // Toggle LED
    printf("tickCount = %d!\n", tickCount++);
  }
}

int main(void) {
  initGPIO();
  initUSART2();
  initSysTick();
  initKeyboard();
  tm1637_init();

  // Переменная пароля в оперативной памяти (по умолчанию 1234)
  char secretPin[5] = "1234"; 
  char enteredPin[5] = "----";
  uint8_t pinIdx = 0;
  uint32_t resetTimer = 0;

  uint8_t isUnlocked = 0; // Флаг разрешения смены пароля

  printf("Текущий пароль: %s\n", secretPin);
  printf("Управление:\n");
  printf("  [0-9] : ввод цифр\n");
  printf("  [#]   : проверка пароля (открыть замок)\n");
  printf("  [*]   : сохранить введенные 4 цифры как НОВЫЙ пароль (только после верного пароля!)\n\n");

  tm1637_show_text("----");

  while (1) {
    // Возврат в режим ожидания после показа PASS / FAIL / SEt
    if (resetTimer > 0 && tickCount >= resetTimer) {
      resetTimer = 0;
      pinIdx = 0;
      // ВАЖНО: isUnlocked здесь НЕ сбрасываем, чтобы после экрана PASS можно было ввести новый код
      strcpy(enteredPin, "----");
      GPIOA->BRR = (1 << 9) | (1 << 15); // Гасим светодиоды
      tm1637_show_text("----");
    }

    char key = scanKeyboard();

    if (key != '\0' && resetTimer == 0) {
      
      // 1. Ввод цифр (0-9)
      if (key >= '0' && key <= '9' && pinIdx < 4) {
        enteredPin[pinIdx++] = key;
        tm1637_show_text(enteredPin);
        printf("Ввод: %c\n", key);
      }
      
      // 2. СМЕНА ПАРОЛЯ ИЛИ СБРОС (кнопка '*')
      else if (key == '*') {
        if (pinIdx == 4) {
          // Смена разрешена ТОЛЬКО если предварительно был введен верный пароль
          if (isUnlocked) {
            strncpy(secretPin, enteredPin, 4);
            secretPin[4] = '\0';
            
            isUnlocked = 0; // СБРОС ФЛАГА: пароль сохранен, повторно сменить нельзя без авторизации
            
            printf("\n SEt Новый пароль: %s \n", secretPin);
            GPIOA->BSRR = (1 << 9); // Зеленый светодиод подтверждает запись
            tm1637_show_text("SEt ");
            resetTimer = tickCount + 2000;
          } else {
            // Попытка сменить пароль без ввода корректного пароля
            printf("\n FAIL: Смена пароля запрещена! Сначала введите верный пароль.\n");
            GPIOA->BSRR = (1 << 15); // Красный LED ошибки
            tm1637_show_text("FAIL");
            resetTimer = tickCount + 2000;
          }
        } else {
          // Если набрано меньше 4 цифр — это просто сброс текущего набора
          pinIdx = 0;
          strcpy(enteredPin, "----");
          tm1637_show_text("----");
          printf("Ввод отменен\n");
        }
      }
      
      // 3. ПРОВЕРКА ПАРОЛЯ (кнопка '#')
      else if (key == '#') {
        if (pinIdx == 4) {
          if (strncmp(enteredPin, secretPin, 4) == 0) {
            printf("\n PASS (Пароль верный! Теперь можете ввести 4 цифры и нажать '*' для смены)\n");
            isUnlocked = 1; // УСТАНОВКА ФЛАГА: разрешаем смену пароля
            GPIOA->BSRR = (1 << 9); // Зеленый LED
            tm1637_show_text("PASS");
          } else {
            printf("\n FAIL (Неверный пароль)\n");
            isUnlocked = 0; // СБРОС ФЛАГА: при неверном пароле право на смену сразу аннулируется
            GPIOA->BSRR = (1 << 15); // Красный LED
            tm1637_show_text("FAIL");
          }
          resetTimer = tickCount + 2500;
        } else {
          printf("Сначала введите 4 цифры!\n");
        }
      }
    }
  }

  return 0;
}
