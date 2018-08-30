import lesson_6.DOP_DZ.Chat.sample.Controller;
import lesson_6.DOP_DZ.Chat.sample.ServerTCP;
import org.junit.Assert;
import org.junit.Test;

public class TestConnectServerAndClient extends Assert {

    @Test
    public void testConnectServerAndClient(){
        ServerTCP.testConnect();
        assertTrue(Controller.testConnect());
    }
}
