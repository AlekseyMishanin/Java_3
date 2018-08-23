package lesson_4;

/*
3. Написать класс МФУ, на котором возможны одновременная печать и сканирование документов,
при этом нельзя одновременно печатать два документа или сканировать (при печати в консоль
выводится сообщения "отпечатано 1, 2, 3,... страницы", при сканировании тоже самое только
"отсканировано...", вывод в консоль все также с периодом в 50 мс.)
*/

public class DZ_task_3 {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            MultifunctionDevice.print("book"+i);
            MultifunctionDevice.scan("document"+i);
        }
    }
}
/**Класс имитирующий МФУ с возможность одновременной печати и сканирования, но запретом на одновременную печать двух
 * документов или сканирование двух документов. Методы печати и сканирования статические. При вызове печати/сканирования
 * содается новый поток для выполнения поставленной задачи.*/
class MultifunctionDevice {

    private static final Object LOCK_PRINT = new Object();          //объект для синхронизации печати
    private static final Object LOCK_SCAN = new Object();           //объект для синхронизации сканирования

    static public void print(String name){                          //метод печати
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK_PRINT){                                              //синхронизируем блок кода по объекту LOCK_PRINT
                    System.out.println("На печать отрпавлен - " + name);
                    for (int i = 0; i < 5; i++){
                        System.out.println("Отпечатано " + (i+1) + " страницы ...");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Печать завершена.");
                }
            }
        }).start();
    }

    static public void scan(String name){                               //метод сканирования
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK_SCAN){                               //синхронизируем блок кода по объекту LOCK_SCAN
                    System.out.println("Начато сканирование - " + name);
                    for (int i = 0; i < 5; i++){
                        System.out.println("Отсканировано " + (i+1) + " страницы ...");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Сканирование завершено.");
                }
            }
        }).start();
    }
}


