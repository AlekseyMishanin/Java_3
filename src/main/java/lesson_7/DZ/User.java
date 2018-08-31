package lesson_7.DZ;

public class User {

    @BeforeSuite
    public void methodBefore(){
        System.out.println("@BeforeSuite");
    }
    @Test(priority = 1)
    public void method1(){
        System.out.println("@Test(priority = 1)");
    }
    @Test(priority = 2)
    public void method2(){
        System.out.println("@Test(priority = 2)");
    }
    @Test(priority = 3)
    public void method3(){
        System.out.println("@Test(priority = 3)");
    }
    @AfterSuite
    public void methodAfter(){
        System.out.println("@AfterSuite");
    }
}
