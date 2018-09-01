package lesson_6.DOP_DZ.Chat.sample;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthService extends DB_mysql{

    private Statement statement = null;

    public AuthService(){
        try {
            setURL("localhost", "userdb", 3306);    //создаем строку соединения
            Connect("Server", " ");                     //стучимся на сервер
            statement = getConnection().createStatement();
        } catch (SQLException e){
            System.err.println("SQLException: code = " + String.valueOf(e.getErrorCode()+ e.getMessage()));
        }
    }

    public String getNickByLoginAndPass(String login, String pass) {

        String sql = String.format("SELECT nickname, password FROM userDB.main WHERE login = '%s'", login);
        try {
            int myHash = pass.hashCode();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                String nick = resultSet.getString(1);
                int dbhash = resultSet.getInt(2);
                if(myHash==dbhash){
                    return nick;
                } else { return null;}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(String login, String pass, String nick){
        String sql = String.format("INSERT INTO `userdb`.`main` (`login`,`password`,`nickname`) VALUES ('%s' ,'%s', '%s')", login, pass.hashCode(), nick);
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String login){
        String sql = String.format("SELECT id FROM userdb.main WHERE userdb.main.login = '%s'", login);
        try {
            return statement.executeQuery(sql).next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkNick(String nick){
        String sql = String.format("SELECT id FROM userdb.main WHERE userdb.main.nickname = '%s'", nick);
        try {
            return statement.executeQuery(sql).next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
