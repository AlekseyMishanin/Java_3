package lesson_6.DZ;

/*
1. Написать метод, которому в качестве аргумента передается не
пустой одномерный целочисленный массив, метод должен вернуть
новый массив, который получен путем вытаскивания элементов из исходного
массива, идущих после последней четверки. Входной массив должен содержать
хотя бы одну четверку, в противном случае в методе необходимо выбросить
RuntimeException.

2. Написать набор тестов для этого метода (варианта 3-4 входных данных)
вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ]
Написать метод, который проверяет что массив состоит только из чисел 1 и 4.
Если в массиве нет хоть одной 4 или 1, то метод вернет false;
Написать набор тестов для этого метода (варианта 3-4 входных данных)

3. Создать небольшую БД (таблица: студенты; поля: id, фамилия, балл).
Написать тесты для проверки того, что при работе с базой корректно добавляются,
обновляются и читаются записи. Следует учесть что в базе есть заранее
добавленные записи, и после проведения тестов эти записи не должны быть
удалены/изменены/добавлены.
*/



/*
Решение:
1. написан метод updateArray
2. написан метод find1or4inArray и тесты: TestArray1, TestArray2
3. написан тест TestDB
*/


import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        int[] test_arrey = {12,2,4,3,5,5,6,5,7,8,2,5,6,4,3,4,4,5,6,7,1,1,7,78,7,9,0,2,3,4,4,6,7,8,8,9,0,2,1};
        System.out.println(Arrays.toString(test_arrey));
        try {
            System.out.println(Arrays.toString(updateArray(test_arrey)));
        } catch (RuntimeException e){
            e.printStackTrace();
        }


        int[] test_arrey1 = {1,6,4};

        System.out.println(find1or4inArray(test_arrey1));
    }

    public static int[] updateArray (int[] arr) throws RuntimeException{
        final int CONST_INT = 4;
        for (int i = arr.length-1; i >=0 ; i--) {
            if (arr[i]==CONST_INT){
                return Arrays.copyOfRange(arr, i, arr.length);
            }
        }
        throw new RuntimeException("Not found " + CONST_INT);
    }

    public static boolean find1or4inArray(int[] arr){
        boolean find_1=false, find_4=false, find_outher=true;
        for (int i:
        arr) {
            if(i==1) {find_1=true;}
            if(i==4) {find_4=true;}
            if(i!=1&&i!=4) {find_outher=false; break;}
        }
        return find_1&&find_4&&find_outher;
    }
}
