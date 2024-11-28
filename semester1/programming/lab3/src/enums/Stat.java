package enums;

public enum Stat {
    DEATH ("Умер"),
    STRESSED ("Стресс"),
    LONELINESS ("Одиночество"),
    NORMAL ("Нормальный");

    private final String title;
    Stat(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
