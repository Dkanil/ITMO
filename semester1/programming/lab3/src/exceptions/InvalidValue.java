package exceptions;

public class InvalidValue extends IllegalArgumentException {
    public InvalidValue(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "!!!Error InvalidValue: " + super.getMessage();
    }
}
