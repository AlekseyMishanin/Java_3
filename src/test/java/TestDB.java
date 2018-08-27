import org.junit.*;
import org.junit.runners.MethodSorters;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDB extends Assert {
    private static com.mysql.jdbc.Connection connection = null;
    private static Statement statement = null;

    /**Создаем: объект соедниения с БД MySQL; объект для выполениня SQL-запросов, таблицу student. Отключаем автокомит*/
    @BeforeClass
    public static void createConnect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                connection = (com.mysql.jdbc.Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb","User"," ");
                statement = connection.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS `userdb`.`student` (\n" +
                        "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                        "  `fio` VARCHAR(45) NOT NULL,\n" +
                        "  `point` INT NOT NULL,\n" +
                        "                    PRIMARY KEY (`id`));";
                statement.executeUpdate(sql);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**Тестируем добавление записи в таблицу*/
    @Test
    public void test01(){

        String sql = "INSERT INTO `userdb`.`student` (`fio`, `point`) VALUES ('Ivan', 5)";
        int result = 0;
        try {
            result = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(1,result);
    }

    /**Тестируем обновление записи в таблице*/
    @Test
    public void test02(){

        String sql = "UPDATE `userdb`.`student` SET `fio` = 'Vitor' WHERE `id`>0 AND `fio` = 'Ivan' ;";
        int result = 0;
        try {
            result = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(1,result);
    }

    /**Тестируем чтение записей в таблице*/
    @Test
    public void test03(){

        String sql = "SELECT * FROM userdb.student";
        boolean result = false;
        try {
            result = statement.executeQuery(sql).next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(result);
    }

    /**Закрываем объект для выполения SQL-запросов, а также объект соедниения с БД*/
    @AfterClass
    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            statement=null;
            try {
                connection.rollback();              //откатываем изменения
                connection.setAutoCommit(true);
                connection.close();
                connection=null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
