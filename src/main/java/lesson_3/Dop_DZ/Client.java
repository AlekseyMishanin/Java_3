package lesson_3.Dop_DZ;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket clientSocket;
        ObjectOutputStream objectOutputStream = null;
        try {
            clientSocket = new Socket("localhost", 2050);
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Student student = new Student("Bob", 28, 'M');    //создаем объект
            objectOutputStream.writeObject(student);                        //производим сериализацию объекта

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
