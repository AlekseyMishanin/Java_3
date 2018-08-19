package lesson_2.Dz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class dao_base {

    protected String driver         = null; //драйвер JDBC
    protected String url            = null; //строка подключения
    protected Properties properties = null; //свойства подключения

    public dao_base(String driver){
        this.driver=driver;
    }

    /**Регистрация драйвера*/

    protected void RegisterDriverManager(){
        try{
            Class.forName(driver).newInstance();
        } catch (InstantiationException e){e.printStackTrace();}
        catch (IllegalAccessException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
    }

    /**
     * Определение строки подключения url к серверу БД
     * @param host - имя компьютера
     * @param database - наименование БЮ
     * @param port - порт сервера
     * */
    public abstract void setURL(String host, String database, int port);

    /**
     * Получение объекта подключения
     * @return Connection - объект подключения
     * */
    public abstract Connection getConnection();

    /**
     * Регистрация драйвера подключения к серверу СУБД JDBC и определение свойств
     * @param login - логин подключения
     * @param password - пароль подключения
     * */
    public void Connect (String login, String password){
        RegisterDriverManager();
        properties = new Properties();
        properties.setProperty("password", password);
        properties.setProperty("user", login);
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "utf8");
    }

    /**
     * Отключение от сервера БД
     * @param connection - объект подключения
     * */
    public void Disconnect (Connection connection){
        try{
            connection.close();
            connection = null;
        } catch (SQLException e ) {e.printStackTrace();}
    }
}
