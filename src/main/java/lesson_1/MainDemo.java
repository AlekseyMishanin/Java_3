package lesson_1;
/*
1. Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
2. Написать метод, который преобразует массив в ArrayList;
3. Большая задача:
a. Есть классы Fruit -> Apple, Orange;(больше фруктов не надо)
b. Класс Box в который можно складывать фрукты, коробки условно сортируются по типу фрукта,
поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
c. Для хранения фруктов внутри коробки можете использовать ArrayList;
d. Сделать метод getWeight() который высчитывает вес коробки, зная количество фруктов и
вес одного фрукта(вес яблока - 1.0f, апельсина - 1.5f, не важно в каких это единицах);
e. Внутри класса коробка сделать метод compare, который позволяет сравнить текущую коробку
с той, которую подадут в compare в качестве параметра, true - если их веса равны, false в
противном случае(коробки с яблоками мы можем сравнивать с коробками с апельсинами);
f. Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую коробку
(помним про сортировку фруктов, нельзя яблоки высыпать в коробку с апельсинами), соответственно
в текущей коробке фруктов не остается, а в другую перекидываются объекты, которые были в этой коробке;
g. Не забываем про метод добавления фрукта в коробку.*/


/*
Решение:
1. Написан метод swapArray
2. Написан метод convertToArrayList
3. Создан класс Fruit(и наследники Apple и Orangе), а также обобщенный класс Box
*/
import java.util.ArrayList;
import java.util.Arrays;



public class MainDemo {

    public static void main(String[] args) {

        //пример решения первой задачи
        String[] strArr = {"1","dd","3","4","5","6","7"};
        System.out.println(Arrays.toString(strArr));
        swapArray(strArr,2,0);
        System.out.println(Arrays.toString(strArr));

        //пример решения второй задачи
        Double[] f = {1.0,1.5,1.0,2.0,2.0,2.0,3.0,4.0,5.0,5.0,6.0};
        ArrayList<Double> g = new ArrayList<Double>();
        convertToArrayList(g, f);
        System.out.println(g);

        //пример решения третьей задачи
        Apple[] apples = {new Apple(),new Apple(),new Apple(),new Apple(),new Apple(),new Apple()};
        Box<Apple> appleBox = new Box<Apple>(apples);
        Box<Orange> orangeBox = new Box<Orange>();
        orangeBox.SetFruit(new Orange());
        orangeBox.SetFruit(new Orange());
        orangeBox.SetFruit(new Orange());
        orangeBox.SetFruit(new Orange());
        orangeBox.SetFruit(new Orange());
        System.out.println(appleBox.getWeight());
        System.out.println(orangeBox.getWeight());
        System.out.println(orangeBox.compare(appleBox));

        Apple[] apples1 = {new Apple(),new Apple(),new Apple()};
        Box<Apple> appleBox1 = new Box<Apple>(apples1);
        appleBox1.pourBox(appleBox);
        System.out.println(appleBox.getWeight());

    }

    public static <T> void swapArray(T[] array, int a, int b){
        T temp = null;
        temp = array[a];
        array[a]=array[b];
        array[b]=temp;
    }

    public static <T, S extends T> void convertToArrayList (ArrayList<T> arrList, S... array){
        for (S temp: array) {arrList.add(temp);}
    }
}
