package utility;

    /**
     * Класс, представляющий статус выполнения операции.
     */
    public class ExecutionStatus {
        private boolean success;
        private String message;

        /**
         * Конструктор для создания объекта ExecutionStatus.
         * @param success флаг успешности выполнения операции
         * @param message сообщение о результате выполнения операции
         */
        public ExecutionStatus(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        /**
         * Проверяет, была ли операция успешной.
         * @return true, если операция была успешной, иначе false
         */
        public boolean isSuccess() {
            return success;
        }

        /**
         * Возвращает сообщение о результате выполнения операции.
         * @return сообщение о результате выполнения операции
         */
        public String getMessage() {
            return message;
        }
    }