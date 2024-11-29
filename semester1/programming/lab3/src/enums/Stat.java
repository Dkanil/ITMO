package enums;

public enum Stat {
    STRESSED("Стресс"),
    LONELINESS("Одиночество"),
    HAPPINESS("Счастье"),
    ANGRY("Злоба");

    private final String title;
    Stat(String title) {
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
