package lesson_6.Dop_dz_thread;

import java.util.concurrent.Semaphore;

public class Channel extends Obstcles{
    private Semaphore semaphore = new Semaphore(5);

    @Override
    public void move(Ship ship){
        System.out.println("К проливу подплыл " + ship.getName());
        try {
            semaphore.acquire();
            System.out.println("Корабль " + ship.getName() + " плывет через пролив.");
            Thread.sleep(1000);
            semaphore.release();
            System.out.println("Корабль " + ship.getName() + " преодолел пролив.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
