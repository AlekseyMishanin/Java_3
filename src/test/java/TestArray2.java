import lesson_6.DZ.Main;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;



@RunWith(Parameterized.class)
public class TestArray2 extends Assert{

    private int[] array;
    private boolean result;

    public TestArray2(int[] array, boolean result){
        this.array=array;
        this.result=result;
    }

    @Test
    public void test_find1or4inArray(){
        boolean actual = Main.find1or4inArray(this.array);
        assertEquals(result,actual);
    }

    /**Набор входных данных: список, включающий массив целочисленных значений и ожидаемый результат*/
    @Parameterized.Parameters
    public static List<Object[]> isTesting(){
        return Arrays.asList( new Object[][]{
                {new int[]{1,4,1,1,1,4}, true},
                {new int[]{1,4,1,1,5,4}, false},
                {new int[]{4,4,4,4,4,4}, false}
        });
    }
}
