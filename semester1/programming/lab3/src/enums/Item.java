package enums;

public enum Item {
    FOOD("Еда"),
    TUKES("Тюки");

    private final String title;
    Item(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    @Override
    public String toString() {
        return title;
    }
}
