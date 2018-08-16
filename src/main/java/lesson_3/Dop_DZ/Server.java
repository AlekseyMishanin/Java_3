package lesson_3.Dop_DZ;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    private final int PORT = 2050;
    private ServerSocket serverSocket = null;

    Server(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server start.");
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){

        try {
            Socket client = serverSocket.accept();
            System.out.println("Client connect.");
            ObjectInputStream fromClien = new ObjectInputStream(client.getInputStream());
            try {
                Student student = (Student)fromClien.readObject();  //производим десериализацию объекта
                System.out.println("Get object from client:");
                student.display();                                  //выводим содержимое объекта в консоль
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            fromClien.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
