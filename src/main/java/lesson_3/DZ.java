package lesson_3;

/*
Прочитать файл (около 50 байт) в байтовый массив и вывести этот массив в консоль;

Последовательно сшить 5 файлов в один (файлы примерно 100 байт).
Может пригодиться следующая конструкция: ArrayList<InputStream> al = new ArrayList<>(); ...
Enumeration<InputStream> e = Collections.enumeration(al);

Написать консольное приложение, которое умеет постранично читать текстовые файлы (размером > 10 mb).
Вводим страницу (за страницу можно принять 1800 символов), программа выводит ее в консоль.
Контролируем время выполнения: программа не должна загружаться дольше 10 секунд, а чтение – занимать свыше 5 секунд.*/

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class DZ {

    public static void main(String[] args) {
        DZ dz = new DZ();
        dz.ds_task_1();                 //решение первой задачи
        try {
            dz.dz_task_2();             //решение второй задачи
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dz.dz_task_3();             //решение третьей задачи
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ds_task_1(){
        FileInputStream fileInputStream = null;
        byte[] bBuffer = new byte[50];          //создаем массив байт размерностью в 50 байт
        try {
            fileInputStream = new FileInputStream("src/main/java/text_1");        //открываем файл
            try {
                fileInputStream.read(bBuffer);                                          //читаем из файла в буфер
                for (int i = 0; i < bBuffer.length; i++) {
                    System.out.print((char) bBuffer[i]);                                //выводим содержимое буфера
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileInputStream=null;
        }
    }

    public void dz_task_2() throws IOException {
        ArrayList<InputStream> inputStreams = new ArrayList<>();
        try {
            inputStreams.add(new FileInputStream("src/main/java/text_1"));
            inputStreams.add(new FileInputStream("src/main/java/text_2"));
            inputStreams.add(new FileInputStream("src/main/java/text_3"));
            inputStreams.add(new FileInputStream("src/main/java/text_4"));
            inputStreams.add(new FileInputStream("src/main/java/text_5"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        SequenceInputStream sis = new SequenceInputStream(Collections.enumeration(inputStreams));
        BufferedReader bf = new BufferedReader(new InputStreamReader(sis));
        String str = null;
        while ((str=bf.readLine())!=null){System.out.println(str);}
        bf.close(); bf = null;
        sis.close(); sis = null;
    }

    public void dz_task_3() throws IOException {
        final int LEN = 1800;               //размер страницы
        byte[] bBuf = new byte[LEN];        //массив байт для печати страницы в консоль
        String nameFile = null;             //полный путь к файлу

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        RandomAccessFile fileInputStream = null;    //переменная для чтения файла с произвольным доступом
        System.out.println("Введите полный путь к файлу: ");
        while (true){
            try{
                nameFile = bf.readLine();
                fileInputStream =   new RandomAccessFile(nameFile,"r"); //содзаем объект только для чтения
                break;
            }catch (IOException e){System.out.println("Файл не найден. Проверьте данные и попробуйте еще раз");}
        }
        System.out.println("Файл состоит из " + fileInputStream.length()/LEN + " страниц.");
        while (true){
            try{
                System.out.println();
                System.out.println("Выберите страницу для печати в консоль(для выхода введите не числовой символ)");
                String namberLine = null;           //номер выбранной строки
                namberLine = bf.readLine();         //считываем данные с консоли
                int stroka = Integer.parseInt(namberLine) - 1;  //проверяем тип данных: число или не число
                    fileInputStream.seek(stroka*LEN);       //перемещаем указатель на новую позицию
                    fileInputStream.read(bBuf);                 //читаем данные в буфер
                long read_start = System.currentTimeMillis();
                for (int i = 0; i < bBuf.length; i++) {
                    System.out.print((char) bBuf[i]);           //печатаем данные в консоль
                }
                if(System.currentTimeMillis()-read_start>5000){
                    System.out.println("Чтение данных заняло более 5-и секунд.\nОторвите разработчику руки.");
                }
            }catch (NumberFormatException e) {break;}

        }
    }

}
