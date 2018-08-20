package lesson_4;

/*
 2. Написать совсем небольшой метод, в котором 3 потока построчно пишут данные в файл
(штук по 10 записей, с периодом в 20 мс)
*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DZ_task_2 {

    public static void main(String[] args) {
        new DZ_task_2().smallMethod();
    }

    /**Меленький метод. В теле метода запускается цикл, создающий 3-и потока.
     * Каждый поток пишет построчно в файл. Для переключения между потоками используется Thread.sleep(20);*/
    public void smallMethod(){
        try {
            //создаем байтовый поток вывода в файл
            FileOutputStream fileWriter = new FileOutputStream(new File("src/main/java/lesson_4/","writeFromThread.txt"), true);

            for (int i = 0; i < 3; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            String str = i + " string from Thredad - " + Thread.currentThread().getName() + '\n'; //создаем строку
                            synchronized (fileWriter){                  //синхронизируем блок кона по объекту потока
                                try {
                                    fileWriter.write(str.getBytes());   //пишем в файл массив байт
                                    try {
                                        Thread.sleep(20);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
