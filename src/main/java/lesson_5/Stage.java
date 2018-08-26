package lesson_5;

public abstract class Stage {
    protected int length;
    protected String description;
    protected boolean last_obstacle; //переменная для определения последнего препятствия
    public String getDescription() {
        return description;
    }
    public abstract void go(Car c);
}
