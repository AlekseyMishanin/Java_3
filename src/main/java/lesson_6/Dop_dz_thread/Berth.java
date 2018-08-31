package lesson_6.Dop_dz_thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Berth extends Obstcles{

    protected int speed;
    protected AtomicInteger stock;
    protected int count_location;
    protected String type;
    protected Semaphore semaphore;

    public Berth(int stock, String type, String namePort) {
        this.stock.addAndGet(stock);
        this.type = type;
        this.speed = 100;
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public int getCount_location() {
        return count_location;
    }

    public void setCount_location(int count_location) {
        this.count_location = count_location;
        setSemaphore(new Semaphore(count_location));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

     public int loading(Ship ship) {
        int result = 0;
         try {
             for (int i = 0; i <= ship.getCapacity(); i+=100, result=i) {
                 Thread.sleep(1000);
                 if(stock.get()>0){
                    stock.set(stock.get()-speed);
                    System.out.println("На " + ship.getName() + " загружено 100ед.");
                 }
             }
             System.out.println("Погрузка на " + ship.getName() + " завершена.");
         } catch (InterruptedException e) {
             e.printStackTrace();
         } finally {
             return result;
         }
     };

    @Override
    public void move(Ship ship) {
        if(getStock()>0){
            System.out.println("Корабль " + ship.getName() + " плывет в порт.");
            try {
                semaphore.acquire();
                System.out.println("Корабль " + ship.getName() + " пришвартовался. Начался процесс погрузки");
                loading(ship);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
class portClothing extends Berth{
    portClothing(){
        super(5900,"clothes");
    }
}
class portFood extends Berth{
    portFood(){
        super(2700,"food");
    }
}
class portFuel extends Berth{
    portFuel(){
        super(8500,"fuel");
    }
}*/


