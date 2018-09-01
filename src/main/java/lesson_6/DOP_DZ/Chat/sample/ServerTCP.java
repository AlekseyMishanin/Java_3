package lesson_6.DOP_DZ.Chat.sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.Vector;

public class ServerTCP {

    private Vector<ClientHandler> clients;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    final int PORT = 1501;
    private AuthService authService = null;

    public ServerTCP(){
        try {
            authService = new AuthService();
            clients = new Vector<>();
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен");
            while (true){
                socket = serverSocket.accept();
                System.out.println("Клиент подключился");
                //clients.add(new ClientHandler(this, socket, authService)); //устаревшая часть кода. Оставил на память.
                new ClientHandler(this, socket, authService);
            }
        } catch (IOException e) {e.printStackTrace();}
        finally {
            try{
                socket.close();
            } catch (IOException e){e.printStackTrace();}
            try{
                serverSocket.close();
            } catch (IOException e){e.printStackTrace();}
            authService.Disconnect(authService.getConnection());
        }
    }
    public void broadcastMsg(ClientHandler from, String msg) {
        for (ClientHandler o: clients) {
            if(!o.chackBlackList(from.getNick())){
                o.sendMsg(msg);
            }
        }
    }

    //метод для оправки личных сообщений
    void privateMsg(ClientHandler from, String toNick, String msg) {
        ClientHandler client = null;
        if((client=findNick(toNick))!=null){
            client.sendMsg("." + from.getNick() + ": " + msg);
            from.sendMsg("." + from.getNick() + ": " + msg);
        } else {
            from.sendMsg("Пользователь " + toNick + " не авторизован.");
        }
    }

    public void subcribe (ClientHandler clientHandler){
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubcribe (ClientHandler clientHandler){
        clients.remove(clientHandler);
        broadcastClientList();
    }

    //метод для поиска пользователя по нику среди авторизованных пользователей
    public ClientHandler findNick(String nickName){
        for (ClientHandler o: clients) {
            if(o.getNick().equals(nickName)) return o;
        }
        return null;
    }

    //метод загрузки черного списка из БД в переменную blackList в ClientHandler
    public Object loadBlackList (ClientHandler forClient){
        Connection connection = authService.getConnection();
        return AppStatement.loadBlackList(connection,forClient.getNick());
    }

    //метод добавления записи в ЧС (и в БД, и впеременную blackList в ClientHandler)
    public boolean insertStringBlackList (ClientHandler client, String badNick){
        Connection connection = authService.getConnection();
        return AppStatement.insertStringBlackList(connection,client.getNick(),badNick);
    }

    //метод удаления записи из ЧС (и из БД, и из впеременной blackList в ClientHandler)
    public boolean deleteStringBlackList (ClientHandler client, String badNick){
        Connection connection = authService.getConnection();
        return AppStatement.deleteStringBlackList(connection, client.getNick(), badNick);
    }

    public void broadcastClientList(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/clientlist ");
        for (ClientHandler o: clients) {
            stringBuilder.append(o.getNick() + " ");
        }
        String out = stringBuilder.toString();
        for (ClientHandler o: clients) {
            o.sendMsg(out);
        }
    }

    /**Статический метод для тестирования соединения с клиентом. Сервер ожидает сообщение принимает одно
     * сообщение и отправляет его обратно клиенту. После сервер закрывается.*/
    public static void testConnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                Socket socket = null;
                DataInputStream in = null;
                DataOutputStream out = null;
                final int PORT = 1501;
                try {
                    serverSocket = new ServerSocket(PORT);
                    socket = serverSocket.accept();
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    String inStr = in.readUTF();
                    out.writeUTF(inStr);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) in.close();
                        if (out != null) out.close();
                        if (socket != null) socket.close();
                        if (serverSocket != null) serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }}).start();
    }
    public static void main(String[] args) {
        new ServerTCP();
    }

}

