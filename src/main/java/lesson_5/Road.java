package lesson_5;

public class Road extends Stage {
    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }

    public Road(int length, boolean end) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
        this.last_obstacle = end;
    }
    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);
            System.out.println(c.getName() + " закончил этап: " + description);
            //если препятствие последнее, то выводим сообщение о победителе
            if(this.last_obstacle) { System.out.println(c.getName() + "- WIN"); this.last_obstacle = false; }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
