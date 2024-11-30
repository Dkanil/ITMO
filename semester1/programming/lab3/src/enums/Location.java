package enums;

public enum Location {
    DESERT ("Пустыня"),
    SEA ("Море"),
    COAST ("Побережье"),
    ISLAND ("Остров"),
    PORT ("Порт");
    private final String title;
    Location(String title) {
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
