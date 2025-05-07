package com.lab6.common.utility;

import com.lab6.common.validators.ArgumentValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    private Map<String, Pair<ArgumentValidator, Boolean>> commandsData; // Второе значение - true, если команда требует ввода элемента коллекции
    private ExecutionStatus executionStatus;

    public Response(Map<String, Pair<ArgumentValidator, Boolean>> commandsData) {
        this.commandsData = commandsData;
    }

    public Response(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public Map<String, Pair<ArgumentValidator, Boolean>> getCommandsMap() {
        return commandsData;
    }

    @Override
    public String toString() {
        return "Response{" +
                "commandsData=" + commandsData +
                ", executionStatus='" + executionStatus + '\'' +
                '}';
    }
}
