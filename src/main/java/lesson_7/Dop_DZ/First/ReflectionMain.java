package lesson_7.Dop_DZ.First;

/*
1 Доделать программу для проверки ДЗ

Вот, что она должна уметь проверять

3. Написать метод вычисляющий выражение a * (b + (c / d)) и возвращающий результат,
где a, b, c, d – входные параметры этого метода;
4. Написать метод, принимающий на вход два числа, и проверяющий что их сумма лежит
в пределах от 10 до 20(включительно), если да – вернуть true, в противном случае – false;
5. Написать метод, которому в качестве параметра передается целое число, метод должен
напечатать в консоль положительное ли число передали, или отрицательное; Замечание: ноль считаем положительным числом.
6. Написать метод, которому в качестве параметра передается целое число, метод должен
вернуть true, если число отрицательное;
7. Написать метод, которому в качестве параметра передается строка, обозначающая имя,
метод должен вывести в консоль сообщение «Привет, указанное_имя!»;
8. * Написать метод, который определяет является ли год високосным, и выводит сообщение
в консоль. Каждый 4-й год является високосным, кроме каждого 100-го, при этом каждый 400-й – високосный.

*/

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;

public class ReflectionMain {

    public void testJavaDZ() throws Exception{
        File file = new File("D:/javaDz");
        String[] strList = file.list();

        ArrayList<String> fileNAme = new ArrayList<>();

        for (String s:
                strList) {
            String[] locStrArr = s.split("\\.");
            if (locStrArr[1].equalsIgnoreCase("class")){
                fileNAme.add(locStrArr[0]);
            }
        }

        Iterator iterator = fileNAme.iterator();
        while (iterator.hasNext()){
            String name = String.valueOf(iterator.next());
            Class cl = URLClassLoader.newInstance(new URL[]{new File("D:/javaDz").toURL()}).loadClass(name);

            Object obj = cl.getConstructor().newInstance();

            System.out.println("Testing :" + cl.getName());

            for (Method m: cl.getMethods()) {
                //вызываем метод возвращающий int и принимающий 4-е аргумента
                if (m.getReturnType().getName().equalsIgnoreCase("int")&&m.getParameterCount()==4){
                    System.out.println("3. Написать метод вычисляющий выражение a * (b + (c / d)) и возвращающий результат, \n" +
                                         "где a, b, c, d – входные параметры этого метода.");
                    int retultInt = (Integer) m.invoke(obj,3,2,4,2);
                    if (retultInt==12){
                        System.out.println("Решение: метод " + m.getName() + " работает корректно. Задача решена");
                    }
                    else {
                        System.out.println("Решение: метод " + m.getName() + " работает некорректно. Задача не решена");
                    }
                    continue;
                }
                //вызываем метод возвращающий boolean и принимающий 2-е аргумента
                if (m.getReturnType().getName().equalsIgnoreCase("boolean")&&m.getParameterCount()==2){
                    System.out.println("4. Написать метод, принимающий на вход два числа, и проверяющий что их сумма лежит\n" +
                            "в пределах от 10 до 20(включительно), если да – вернуть true, в противном случае – false;");
                    boolean retultBool = (Boolean) m.invoke(obj,20,20);
                    if (retultBool==false){
                        System.out.println("Решение: метод " + m.getName() + " работает корректно. Задача решена");
                    }
                    else {
                        System.out.println("Решение: метод " + m.getName() + " работает некорректно. Задача не решена");
                    }
                    continue;
                }
                //вызываем метод возвращающий void, принимающий 1 аргумент типа int
                if (m.getReturnType().getName().equalsIgnoreCase("void")&&m.getParameterCount()==1&&
                        m.getParameterTypes()[0].getName().equalsIgnoreCase("int")){
                    System.out.println("5. Написать метод, которому в качестве параметра передается целое число, метод должен\n" +
                            "напечатать в консоль положительное ли число передали, или отрицательное; Замечание: ноль считаем положительным числом.");
                    System.out.println("Число 20 - положительное. Решение студента:");
                    m.invoke(obj,20);
                    continue;
                }

                //вызываем метод возвращающий boolean, принимающий 1 аргумент типа int
                if (m.getReturnType().getName().equalsIgnoreCase("boolean")&&m.getParameterCount()==1&&
                        m.getParameterTypes()[0].getName().equalsIgnoreCase("int")){
                    System.out.println("6. Написать метод, которому в качестве параметра передается целое число, метод должен\n" +
                            "вернуть true, если число отрицательное;");
                    boolean retultBool = (Boolean) m.invoke(obj,20);
                    if (retultBool==false){
                        System.out.println("Решение: метод " + m.getName() + " работает корректно. Задача решена");
                    }
                    else {
                        System.out.println("Решение: метод " + m.getName() + " работает некорректно. Задача не решена");
                    }
                    continue;
                }

                //вызываем метод возвращающий void, принимающий 1 аргумент типа String
                if (m.getReturnType().getName().equalsIgnoreCase("void")&&
                       m.getParameterCount()==1&&
                       m.getParameterTypes()[0].getName().equalsIgnoreCase("java.lang.string")){
                    System.out.println("7. Написать метод, которому в качестве параметра передается строка, обозначающая имя,\n" +
                            "метод должен вывести в консоль сообщение «Привет, указанное_имя!»;");
                    System.out.println("Решение: Привет, Василий!\nРешение студента: ");
                    m.invoke(obj,"Василий");
                    continue;
                }

                //вызываем метод findLeapYear, содержащий решение задачи с высокосным годом
                if (m.getName().equalsIgnoreCase("findLeapYear")){
                    System.out.println("8. * Написать метод, который определяет является ли год високосным, и выводит сообщение\n" +
                            "в консоль. Каждый 4-й год является високосным, кроме каждого 100-го, при этом каждый 400-й – високосный.");
                    System.out.println("Решение студента: ");
                    m.invoke(obj);
                    continue;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        new ReflectionMain().testJavaDZ();

    }
}
