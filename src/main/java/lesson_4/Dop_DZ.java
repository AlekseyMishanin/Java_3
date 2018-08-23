package lesson_4;

/*
1 Создать файл (наполнить рандомными данными)
2 Создать 10 потоков для чтения данных из файла (чтение из файла, вывод на консоль)
3 Сделать ограничение, что одновременно может читать только 4 потока
4 Все потоки должны прочитать данные из файла
*/

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Dop_DZ {

    private static File file = null;                //переменная под объект файла
    private static final int COUNT_THREAD = 10;     //константа - количество потоков
    private static final int COUNT_LIMIT = 4;       //константа - лимит на одновременно работающие потоки
    private static Semaphore SEMAPHORE = null;      //переменная под объект синхронизации ограничивающий доступ к общему ресурсу несколькими потоками при помощи счетчика

    /**Метод создания объекта файла и наполнения файла случайным содержимым*/
    public static void crate_file(){
        file = new File ("src/main/java/lesson_4","randomFile.txt");
        try (FileOutputStream out = new FileOutputStream(file);){
            for (int i = 0; i < 100; i++) {
                out.write((int)(Math.random()*100));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Класс инкапсулирующий работу с файлом*/
    public static class MyThread implements Runnable{

        private int threadNumber;   //номер потока

        public MyThread(int threadNumber){
            this.threadNumber=threadNumber;
        }

        @Override
        public void run(){
            System.out.printf("Поток %s готов к чтению файла.\n",threadNumber);
            try {
                SEMAPHORE.acquire();                                                    //запрашиваем разрешение на дальнейшую работу. Уменьшаем значение счетчика на 1
                System.out.printf("Поток %s приступил к чтению файла.\n",threadNumber);
                try (FileInputStream in = new FileInputStream(file);){                  //создаем объект потока для чтения из файла
                    byte[] buffer = new byte[in.available()];                           //создаем массив байт
                    in.read(buffer);                                                    //читаем данный из файла в массив байт
                    System.out.printf("Поток %s завершил чтение файла. Содержимое файла:\n" + Arrays.toString(buffer) + '\n', threadNumber);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SEMAPHORE.release();                                                    //завершаем работу. Увеличиваем значение счетчика на 1
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        crate_file();
        SEMAPHORE = new Semaphore(COUNT_LIMIT,true);
        for (int i = 0; i < COUNT_THREAD; i++) {
            new Thread(new MyThread(i)).start();

        }
    }
}
