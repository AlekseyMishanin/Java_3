package lesson_7.DZ;


/*
1. Создать класс, который может выполнять «тесты», в качестве тестов выступают классы с
наборами методов с аннотациями @Test. Для этого у него должен быть статический метод start(),
которому в качестве параметра передается или объект типа Class, или имя класса. Из «класса-теста»
вначале должен быть запущен метод с аннотацией @BeforeSuite, если такой имеется, далее запущены
методы с аннотациями @Test, а по завершению всех тестов – метод с аннотацией @AfterSuite. К каждому
тесту необходимо также добавить приоритеты (int числа от 1 до 10), в соответствии с которыми будет
выбираться порядок их выполнения, если приоритет одинаковый, то порядок не имеет значения. Методы
с аннотациями @BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре, иначе
необходимо бросить RuntimeException при запуске «тестирования».
(Это домашнее задание никак не связано с темой тестирования через JUnit и использованием этой библиотеки,
то есть проект пишется с нуля)
*/



import java.lang.reflect.Method;

public class TestClass {

    /**
     * Метод запуска тестов. Принимает на вход класс, инициализирует локальную переменную класса и вызывает мето startTest
     * @param cls - класс
     * */
    public static void start(Class cls){
        try {
            Class cl = cls;
            startTest(cl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод запуска тестов. Принимает на вход имя класса, загружает класс и инициализирует локальную
     * переменную класса и вызывает мето startTest
     * @param clsName - имя класса
     * */
    public static void start(String clsName)  {
        try {
            Class cl = Class.forName(clsName);
            startTest(cl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Тестирование методов класса. В теле метода запускается цикл из 3-х итераций:
     * 1 - вызываются методы помеченные аннотацией BeforeSuite;
     * 2 - вызываются методы помеченные аннотацией Test в соответствии с приоритетом;
     * 3 - вызываются методы помеченные аннотацией AfterSuite;
     * @param cls - класс
     * */
    private static void startTest(Class cls) throws Exception{
        Method[] method = cls.getMethods();     //массив методов класса
        Object obj = cls.newInstance();         //экземпляр класса
        int ok_test = 0;                        //кол-во успешно пройденных тестов
        int fail_test = 0;                      //кол-во проваленных тестов
        int countBeforeSuite = 0;               //кол-во методов помеченных BeforeSuite
        int countAfterSuite = 0;                //кол-во методов помеченных AfterSuite
        //подсчитываем кол-во методов помеченных аннотацией BeforeSuite
        for (Method m: method) { if(m.isAnnotationPresent(BeforeSuite.class)) {countBeforeSuite++;}}
        //подсчитываем кол-во методов помеченных аннотацией AfterSuite
        for (Method m: method) { if(m.isAnnotationPresent(AfterSuite.class)) {countAfterSuite++;}}
        //методов помеченных BeforeSuite или AfterSuite более одного выбрасываем исключение
        if(countBeforeSuite>1) {throw new RuntimeException("@BeforeSuite > 1");}
        if(countAfterSuite>1) {throw new RuntimeException("@AfterSuite > 1");}
        for (int i = 0; i < 3; i++) {
            switch (i){
                //на первой итерации вызываем метод с аннотацией BeforeSuite. Если метод отработал корректно -
                //увеличиваем счетчик успешных тестов. В противном случае увеличиваем счетчика проваленных тестов
                case 0: {
                    for (Method m : method) {
                        if (m.isAnnotationPresent(BeforeSuite.class)) {
                            try {
                                m.invoke(obj);
                                ok_test++;
                            } catch (Exception e) {
                                fail_test++;
                            }
                            break;
                        }
                    }
                    ;
                    break;
                }
                //на второй итерации вызываем метод с аннотацией Test.
                // Внешний цикл проходит последовательно по всем возможным приоритетам.
                // Вложенный цикл вызывает методы с приоритетом соответствующем значению внешнего цикла
                // Если метод отработал корректно -
                //увеличиваем счетчик успешных тестов. В противном случае увеличиваем счетчика проваленных тестов
                case 1:{
                    for (int j = 0; j < 10; j++) {
                        for (Method m: method) {
                            if(m.isAnnotationPresent(Test.class)&&m.getAnnotation(Test.class).priority()==(j+1)) {
                                try {
                                    m.invoke(obj);
                                    ok_test++;
                                } catch (Exception e){
                                    fail_test++;
                                }
                            }
                        };
                    }
                    break;
                }
                //на третьей итерации вызываем метод с аннотацией AfterSuite. Если метод отработал корректно -
                //увеличиваем счетчик успешных тестов. В противном случае увеличиваем счетчика проваленных тестов
                case 2:{
                    for (Method m: method) {
                        if(m.isAnnotationPresent(AfterSuite.class)) {
                            try {
                                m.invoke(obj);
                                ok_test++;
                            } catch (Exception e){
                                fail_test++;
                            }
                            break;
                        }
                    };
                    break;
                }
            }
        }
        System.out.println("Tests succesfully - " + ok_test);
        System.out.println("Tests failed - " + fail_test);
    }

    public static void main(String[] args) {

        try {
            Class cl = Class.forName("lesson_7.DZ.User");
            TestClass.start(cl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
