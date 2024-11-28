package enums;

public enum Location {
    DESERT ("Пустыня"),
    SEA ("Море"),
    ISLAND ("Остров");
    private String title;
    Location(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Location{" +
                "title='" + title + '\'' +
                '}';
    }
}
