package lesson_4;

/*
1. Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз,
порядок должен быть именно ABСABСABС. Используйте wait/notify/notifyAll.
*/

public class DZ_task_1 {

    public static void main(String[] args) {

        ThreadABC t1 = new ThreadABC('A');
        ThreadABC t2 = new ThreadABC('B');
        ThreadABC t3 = new ThreadABC('C');
    }
}

/** Решение первой задачи основного ДЗ*/
class ThreadABC implements Runnable{

    private static final Object LOCK = new Object();    //объект используемый для синхронизации блока кода

    private static Character currentChar;               //статическая переменная для текущего значения символа
    private Character innerChar;                        //внутренняя переменная для значения символа переданного при создании объекта
    Thread th;

    static {
        currentChar = 'A';                              //статический блок инициализации. Присваиваем статической переменной начальное значение
    }

    ThreadABC(Character value){
        this.innerChar = value;
        th = new Thread(this);
        th.start();
    }

    @Override
    public void run(){
        for (int i = 0; i < 5; i++) {
            synchronized (LOCK){                                                    //синхронизируем блок кода по объекту LOCK
                while(currentChar!=innerChar) {                                     //если символ в статической переменной отличен от значения внутренней переменной (условие заключено в цикл, чтобы избежать ложной активизации)
                    try {
                        LOCK.wait();                                                //то вызывающий поток засыпает, освобождая монитор объекта LOCK
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                switch (innerChar){                                                 //если символы переменных равны, то выводим символ в консоль и меняем значение статической переменной
                    case 'A': System.out.print(innerChar); currentChar='B'; break;
                    case 'B': System.out.print(innerChar); currentChar='C'; break;
                    case 'C': System.out.print(innerChar); currentChar='A'; break;
                }
                LOCK.notifyAll();                                                   //возобновляем работу всех потоков
            }
        }
    }
}
