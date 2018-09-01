package lesson_6.DOP_DZ.Chat.sample;

//базовый класс модуля доступа к БД

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public abstract class DB_base {

    protected String driver = null;         //драйвер
    protected String url = null;            //строка подключения
    protected Properties properties = null; //свойства подключения

    public DB_base(String driver){
        this.driver=driver;
    }

    //регистрация драйвера
    protected void RegisterDriveManager(){
        try{
            Class.forName(driver).newInstance();
        }catch (Exception e){ e.printStackTrace();}
    }

    //определение строки подключения
    public abstract void setURL (String host, String database, int port);

    //получение объекта подключения
    public abstract Connection getConnection();

    //регистрация драйвера подключения к сервуру СУБД JDBC  и определение свойств
    public void Connect(String login, String password){

        RegisterDriveManager();
        properties = new Properties();
        properties.setProperty("password", password);
        properties.setProperty("user", login);
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "utf8");

    }

    //отключение от сервера БД
    public void Disconnect(Connection connection){
        try{
            connection.close();
            connection=null;
        }catch (SQLException e) {
            System.err.println("SQLException: code = " + String.valueOf(e.getErrorCode()+ e.getMessage()));
        }
    }

    //функция выполнениея sql-запроса
    public boolean execSQL(final String sql){
        boolean result = false;
        try{
            if(getConnection()!=null){
                Statement statement = getConnection().createStatement();
                statement.execute(sql);
                statement.close();
                statement=null;
                result=true;
            }
        } catch (SQLException e){
            System.err.println("SQLException: code = " + String.valueOf(e.getErrorCode()+ e.getMessage()));
            System.err.println("SQL: " + sql);
        }
        return result;
    }
}

