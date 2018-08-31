package lesson_6.Dop_dz_thread;

public class Ship {

    private int capacity;
    private String name;

    Ship(String name) {
        this.capacity = 500;
        this.name=name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
