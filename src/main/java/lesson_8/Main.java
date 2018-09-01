package lesson_8;

import java.util.Arrays;

public class Main {

    /**
     * Метод заполнения матрицы заданного размера целочисленными значениями по спирали
     * @param arr - искходная матрица
     * @param m - кол-во столбцов в матрице
     * @param n - кол-во строк в матрице
     * @param value - число размещаемое в матрице
     * @param step - смещение массива*/
    public static void recursio(int[][] arr, int step, int n, int m, int value){

        int locN = n-step; //переменная определяющая кол-во столбцов внешнего контура
        int lokM = m-step; //переменная определяющая кол-во строк внешнего контура
        //t2 - номер строки внешнего контура
        //t1 - номер столбца внешнего контура
        //(n-1)*(m-1) - периметр внешнего контура. Максимальное значение элементов на данном уровне
        for (int i = 0,t1=0,t2=0; i < (n-1)*(m-1); i++) {
            value++;                //увеличиваем значение переменной, для последующего размещения в матрице
            if(i>=0&&i<locN){arr[step+t2][step+t1++]=value; continue;} //пишем число в верхнюю строку в соответствии со смещением
            if(i>=locN&&i<(locN+lokM-1)){if(t2==0){t2++;}arr[step+t2++][step+t1-1]=value; continue;} //пришем число в правый столбец в соответствии со смещением
            if(i>=(locN+lokM-1)&&i<((locN-1)*2+lokM)){if(t1==locN){t1-=2;} arr[step+(t2-1)][step+(t1--)]=value; continue;} //пишем число в нижнюю строку в соответствии со смещением
            if(i>=((locN-1)*2+lokM)){if(t2==lokM){t2-=2;t1=0;} arr[step+t2--][step+t1]=value; continue;} //пришем число в левый столбец в соответствии со смещением
        }
        if(value==arr.length*arr[0].length) return; //если значение переменной value равно кол-ву элементов в матрице, тогда выходим из метода
        //рекурсивно вызываем текущий метод, увеличивая смешение на 1 и уменьшая длину и высоту матрицы на 1
        recursio(arr,step+1,n-1,m-1,value);
    }

    public static void main(String[] args) {
        int value = 0;
        int n = 4;
        int m = 7;
        int[][] arr = new int [m][n];

        recursio(arr,0,n,m,0);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(Arrays.toString(arr[i]));
        }
    }
}
