package lesson_7.Dop_DZ.Second;


/*
2 На основе класса Student нужно добавить объект в БД
*/

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class MainDB {
    static Connection connection;
    static Statement statement;

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/userdb", "User", " ");
            statement = connection.createStatement();
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try {
            statement.close();
            connection.close();
            statement=null;
            connection=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void prepareTable(Class c) throws SQLException{

        if(!c.isAnnotationPresent(xTable.class)){throw new RuntimeException("xTable is missed");}

        HashMap<Class,String> hm = new HashMap<>();
        hm.put(int.class,"INTEGER");
        hm.put(String.class, "TEXT");

        try{
            connect();

            String tableName = ((xTable)c.getAnnotation(xTable.class)).name();

            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName + ";");

            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (";

            Field[] fields = c.getDeclaredFields();
            for (Field field: fields) {
                if(field.isAnnotationPresent(xField.class)){
                    query += field.getName() + " " + hm.get(field.getType()) + ", ";
                }
            }
            query = query.substring(0,query.length()-2);
            query += ");";

            statement.executeUpdate(query);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            disconnect();
        }

    }

    public static void insertStudent(Student student) throws SQLException{

        Class cls = student.getClass();

        if(!cls.isAnnotationPresent(xTable.class)){throw new RuntimeException("xTable is missed");}

        try{
            connect();
            //получаем имя таблицы
            String tableName = ((xTable)cls.getAnnotation(xTable.class)).name();

            //начинаем писать sql-запрос на добавление записи
            String query = "INSERT INTO  " + tableName + " (";

            //проходим по всем полям объекта и добавляем в текст запроса именя полей
            Field[] fields = cls.getDeclaredFields();
            for (Field field: fields) {
                if(field.isAnnotationPresent(xField.class)){
                    query += field.getName() + ", ";
                }
            }

            //обрезаем лишнюю запятую
            query = query.substring(0,query.length()-2);
            query += ") VALUES (";

            //проходим все поля и извлекаем соответствующие значения поля объекта. Полеченное значение добавляем в текст запроса
            for (Field field: fields) {
                if(field.isAnnotationPresent(xField.class)){
                    if(field.getType().getName().equalsIgnoreCase("java.lang.String")){
                        query += "\"" + String.valueOf(field.get(student)) + "\", ";
                    } else {
                        query += field.getInt(student) + ", ";
                    }
                }
            }
            query = query.substring(0,query.length()-2);
            query += ");";
            statement.executeUpdate(query);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            disconnect();
        }

    }

    public static void main(String[] args) {
        try {
            //MainDB.prepareTable(Student.class);
            Student student = new Student(1,"Vitor", 15, "none");
            MainDB.insertStudent(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
