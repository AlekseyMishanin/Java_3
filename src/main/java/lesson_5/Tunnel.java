package lesson_5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private static Semaphore SEMAPHORE;  //переменная для объекта синхронизации, ограничивающего кол-во потоков, которые могут зайти в тоннель
    public Tunnel(int count_cars) {
        SEMAPHORE = new Semaphore(count_cars/2); //создаем объект. В туннель не может заехать одновременно больше половины участников
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                SEMAPHORE.acquire(); //запрашиваем у семафора разрешение
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                SEMAPHORE.release(); //освобождаем разрешение у семафора
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
