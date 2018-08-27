import lesson_6.DZ.Main;
import org.junit.Assert;
import org.junit.Test;


public class TestArray1 extends Assert {

    /**Проверяем работу метода updateArray при условии наличия 4 во входных данных*/
    @Test()
    public void test_updateArray_Ok (){
        assertNotNull(Main.updateArray(new int[]{1,23,3,5,5,6,7,5,5,3,2,2,2,4,5,6,7,8}));
    }

    /**Проверяем работу метода updateArray при условии отсутствия 4 во входных данных*/
    @Test(expected = RuntimeException.class)
    public void test_updateArray_ExceptionOk (){
        Main.updateArray(new int[]{1,23,3,1,5,6,7,9,1,3,2,2,9,3,5,6,7,8});
    }

}
