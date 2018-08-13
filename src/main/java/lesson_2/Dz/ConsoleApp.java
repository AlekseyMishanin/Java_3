package lesson_2.Dz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class ConsoleApp {

        private dao_mysql db;

    public ConsoleApp(){
        db = new dao_mysql();
        db.setURL("localhost","userdb",3306);
        db.Connect("User"," ");
        create_table(db.getConnection());
        update_table(db.getConnection());

    }
    /**
     * Метод принимает через консоль служебные команды и параметры для выполнения команд. Если служебная команда
     * распознана, то вызывается метод для выполнения данной команды
     * */
    public void workApp() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Список доступных действий:\n" +
                    "Узнать цену товара (например, «/цена товар545»);\n" +
                    "Изменить цену товара (например, «/сменитьцену товар10 10000»);\n" +
                    "Вывести товара заданного ценового диапазона (например, «/товарыпоцене 100 600»)\n" +
                    "Завершение работы /выход");
            try {
                String str_command = bf.readLine();
                String[] tokens = str_command.split(" ");
                switch (tokens[0]) {
                    case "/цена":
                        knowPrice(db.getConnection(), tokens[1]);
                        break;
                    case "/сменитьцену":
                        changePrice(db.getConnection(), tokens[1], tokens[2]);
                        break;
                    case "/товарыпоцене":
                        findProduct(db.getConnection(), tokens[1], tokens[2]);
                        break;
                    case "/выход":
                        return;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**Метод создает структуру таблицы в БД если таблица отсутствует в БД
     * @param connection - объект соединения с БД
     * */
    public void create_table(Connection connection){

        Statement statement = null;
        try{
            statement = connection.createStatement();
            String sql_text = "CREATE TABLE IF NOT EXISTS `userdb`.`product` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `prodid` INT NOT NULL,\n" +
                    "  `title` VARCHAR(45) NOT NULL,\n" +
                    "  `cost` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;";
            statement.executeUpdate(sql_text);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(statement!=null) statement.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**Метод очищает таблицу от данных и наполняет ее новыми данными.
     * @param connection - объект соединения с БД
     * */
    public void update_table(Connection connection){

        Statement statement1 = null;
        PreparedStatement statement2 = null;
        String sql_text = null;
        try{
            statement1 = connection.createStatement();
            sql_text = "TRUNCATE `userdb`.`product`;";
            statement1.executeUpdate(sql_text);
            sql_text = "INSERT INTO `userdb`.`product` (`prodid`, `title`,`cost`) VALUES (?, ?, ?);";
            statement2 = connection.prepareStatement(sql_text); statement2.addBatch();
            connection.setAutoCommit(false);
            for (int i = 0; i < 10000; i++) {
                statement2.setInt(1, i);
                statement2.setString(2, "товар" + i);
                statement2.setInt(3,i*10);
                statement2.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(statement1!=null) {statement1.close();}
                statement1 = null;
                if(statement2!=null) {statement2.close();}
                statement2 = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**Метод осуществляет поиск в таблице записи по наименованию товара.
     * @param connection - объект соединения с БД
     * @param name - наименование товара
     * */
    public void knowPrice(Connection connection, String name){
        PreparedStatement statement = null;
        String sql_text = "SELECT cost FROM userdb.product WHERE title = ?;";
        try {
            statement = connection.prepareStatement(sql_text);
            statement.setString(1,name);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                System.out.println("Такого товара нет.");
            } else {
                System.out.println(name + " " + rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql_text=null;
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            statement = null;
        }
    }

    /**Метод обновляет запись в БД.
     * @param connection - объект соединения с БД
     * @param name - наименование товара
     * @param price - новая цена товара
     * */
    public void changePrice(Connection connection, String name, String price){
        PreparedStatement statement = null;
        String sql_text = "UPDATE userdb.product SET cost = ? WHERE id > 0 AND title = ? ;";
        try {
            statement = connection.prepareStatement(sql_text);
            statement.setInt(1,Integer.parseInt(price));
            statement.setString(2,name);
            int rs = statement.executeUpdate();
            if (rs==0) {
                System.out.println("Что-то пошло не так. Цена осталась прежней.");
            } else {
                System.out.println("Цена на " + name + " составляет - " + price);
            }
        } catch (NumberFormatException e){
            System.out.println("Задано некорректное значение цены.");
            e.getLocalizedMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql_text=null;
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            statement = null;
        }
    }

    /**Метод осуществляет поиск записей, удовлетворяющих диапазону цен.
     * @param connection - объект соединения с БД
     * @param priseStart - нижняя планка цены
     * @param priceEnd - верхняя планка цены
     * */
    public void findProduct(Connection connection, String priseStart, String priceEnd){
        PreparedStatement statement = null;
        String sql_text = "SELECT title AS 'ТОВАР', cost AS 'ЦЕНА'  FROM userdb.product WHERE cost BETWEEN ? AND ? ;";
        try {
            statement = connection.prepareStatement(sql_text);
            statement.setInt(1,Integer.parseInt(priseStart));
            statement.setInt(2,Integer.parseInt(priceEnd));
            ResultSet rs = statement.executeQuery();
            if (rs.wasNull()) {
                System.out.println("Такого товара нет.");
            } else {
                ResultSetMetaData rsMetaData = rs.getMetaData();
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    System.out.print(rsMetaData.getColumnLabel(i) + '\t');
                }
                System.out.println();
                while (rs.next()){
                    System.out.println(rs.getString(1)+'\t' + rs.getString(2));
                }
            }
        } catch (NumberFormatException e){
            System.out.println("Задано некорректное значение цены.");
            e.getLocalizedMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql_text=null;
            if(statement!=null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            statement = null;
        }
    }
}
