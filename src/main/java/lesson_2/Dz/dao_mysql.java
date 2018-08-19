package lesson_2.Dz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dao_mysql extends dao_base{

    private com.mysql.jdbc.Connection connection = null;

    public dao_mysql(){
        super("com.mysql.jdbc.Driver");
    }

    @Override
    public void setURL(String host, String database, int port){
        if (database.length()>0){
            this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        }else {
            this.url = "jdbc:mysql://" + host + ":" + port;
        }
    }

    @Override
    public Connection getConnection(){
        return connection;
    }

    @Override
    public  void Connect (String login, String password){
        super.Connect(login,password);
        try{
            connection = (com.mysql.jdbc.Connection)DriverManager.getConnection(url, properties);
        }catch (SQLException e){connection=null; e.printStackTrace();}
    }
}
