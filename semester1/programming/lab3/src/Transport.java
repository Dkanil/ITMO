import enums.Location;

public record Transport(String name) {
    public void move(Location location){
        System.out.printf("Благодаря %s удаётся добраться в %s", name, location);
    }
}
