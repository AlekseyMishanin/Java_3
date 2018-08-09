package lesson_1;

public class Fruit {

    private double WEIGHT;

    protected void setWEIGHT(double WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public double getWEIGHT() {
        return WEIGHT;
    }

}

class Apple extends Fruit{
    private final double WEIGHT = 1.0f; //вес яблока

    Apple(){
        super.setWEIGHT(WEIGHT);        //передаем значение веса суперклассу
    }

}

class Orange extends Fruit{
    private final double WEIGHT = 1.5f; //вес апельсина

    Orange(){
        super.setWEIGHT(WEIGHT);        //передаем значение веса суперклассу
    }

}