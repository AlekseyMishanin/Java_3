package lesson_6.DOP_DZ.Chat.sample;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AppStatement {

    public static Object loadBlackList(Connection connection, String nick) {
        Set<String> blackList = new HashSet<>();
        String sqlText = String.format("SELECT bad_nick FROM userdb.blacklist " +
                "INNER JOIN userdb.main ON userdb.main.id = userdb.blacklist.id_nick " +
                "WHERE userdb.main.nickname = '%s'", nick);
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlText);){
            while (resultSet.next()){
                System.out.println(resultSet.getString(1));
                blackList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blackList;
    }


    public static boolean insertStringBlackList(Connection connection, String fromNick, String badNick){
        String sqlText = String.format("INSERT INTO `userdb`.`blacklist` (`id_nick`,`bad_nick`) " +
                "VALUES ((SELECT id FROM userdb.main " +
                "WHERE nickname = '%s'), '%s')", fromNick, badNick);
        boolean result = false;
        try(Statement statement = connection.createStatement();){
            statement.execute(sqlText);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static boolean deleteStringBlackList(Connection connection, String fromNick, String badNick){
        String sqlText = String.format("DELETE FROM userdb.blacklist " +
                "WHERE id_nick IN (SELECT id FROM userdb.main WHERE nickname = '%s') AND id_blackList>0 " +
                "AND bad_nick = '%s'", fromNick, badNick);
        boolean result = false;
        try(Statement statement = connection.createStatement();){
            statement.execute(sqlText);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result;
    }
}
