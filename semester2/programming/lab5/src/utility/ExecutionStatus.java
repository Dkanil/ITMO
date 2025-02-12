package utility;

public class ExecutionStatus {
    private boolean success;
    private String message;

    public ExecutionStatus(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
