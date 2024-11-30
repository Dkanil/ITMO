package exceptions;

public class NoItems extends StringIndexOutOfBoundsException {
    public NoItems(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "!!!Error NoItems: " + super.getMessage();
    }
}
