package lesson_1;

import java.util.ArrayList;

public class Box<T extends Fruit> {

    private ArrayList<T> box = new ArrayList<T>();      //определяем ящик с фруктами

    //если конструктору передан массив с фруктами, то наполняем ящик фруктами из массива
    Box(T... arrFruit){
        for (T fruit:
                arrFruit) {
            this.box.add(fruit);
        }
    }


    public double getWeight(){
        //если ящик пуст, то возвращаем 0, иначе умножаем кол-во фруктов в ящике на вес одного фрукта
        return box.isEmpty() ? 0.0 : box.size()* box.get(0).getWEIGHT();
    }


    public boolean compare(Box<?> o) {
        //если вес ящиков равен, то возвращаем true, в противном случае - false
        return this.getWeight()==o.getWeight() ? true : false;
    }

    public void SetFruit(T fruit){
        //добавляем один фрукт в ящик
        box.add(fruit);
    }

    //пересыпаем фрукты из текущего ящика в ящик, переданный в качестве аргумента методу
    public void pourBox(Box<T> box){
        for (T fruit:
             this.box) {
            box.SetFruit(fruit);
        }
        this.box.removeAll(this.box);
    }
}
