package lesson_6.DOP_DZ.Chat.sample;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.logging.*;



public class ClientHandler implements Runnable{

    private Socket socket = null;
    private ServerTCP server = null;
    private Thread thread = null;
    private MessageObject userMsg = null;
    private AuthService authService = null;
    private String nick = null;
    private Set<String> blackList = null;
    private static final Logger logger = Logger.getLogger("");


    public ClientHandler(ServerTCP server, Socket socket, AuthService authService){

        this.authService=authService;
        this.server = server;
        this.socket = socket;
        this.userMsg = new MessageObject(socket);
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void run(){

        while(true){
            String inStr = userMsg.read();
            if (inStr.startsWith("/addUser ")){
                String[] tokens = inStr.split(" ");
                if(authService.checkLogin(tokens[1])){sendMsg("Логин занят.");}
                else if(authService.checkNick(tokens[3])){sendMsg("Ник занят.");}
                else{
                    authService.addUser(tokens[1],tokens[2], tokens[3]);
                    createLogAddOrRegUser("/addUser", tokens[3]);
                    sendMsg("Регистрация прошла успешно.");
                }
            }
            if(inStr.startsWith("/auth")){
                String[] tokens = inStr.split(" ");
                String nickname = authService.getNickByLoginAndPass(tokens[1], tokens[2]);
                if(server.findNick(nickname)!=null){                            //проверяем ник пользователя
                    userMsg.write("Данный пользователь уже авторизован");   //если пользователь с данным ником зарегистрирован, то выводим сообщение
                }else if(nickname!=null){
                    userMsg.write("/authok " + nickname);
                    createLogAddOrRegUser("/authok", nickname);
                    nick = nickname;
                    server.subcribe(this);
                    this.blackList = (HashSet<String>)server.loadBlackList(this); //загружаем черный список
                    break;
                } else {userMsg.write("Неверный логин/пароль");}
            }
            if(inStr.equals("/end")) {
                userMsg.write("/serverclose");
                close();
                return;
            }
        }

        while(true) {
            String inStr = userMsg.read();
            if (inStr.startsWith("/w ")) {
                String[] tokens = inStr.split(" ", 3);
                if (!blackList.contains(tokens[1])) {
                    server.privateMsg(this, tokens[1], tokens[2]/*inStr.substring((tokens[0]+tokens[1]+" ").length())*/);
                } else {
                    sendMsg("Пользователь " + tokens[2] + " в ЧС.");
                }
            } else {
                if (inStr.equals("/end")) {
                    userMsg.write("/serverclose");
                    break;
                } else if (inStr.startsWith("/blackList")) {
                    String[] strBlack = inStr.split(" ");
                    if (!blackList.contains(strBlack[1])) {
                        if (server.insertStringBlackList(this, strBlack[1])) {
                            blackList.add(strBlack[1]);
                            sendMsg("Вы добавили пользователя " + strBlack[1] + " в ЧС");
                        } else {
                            sendMsg("SQL запрос на добавление записи в БД не выполнен. Пользователь не добавлен в ЧС");
                        }
                    } else {
                        sendMsg("Пользователь " + strBlack[1] + " ранее был добавлен в ЧС");
                    }

                } else if (inStr.startsWith("/whiteList")) {
                    String[] strBlack = inStr.split(" ");
                    if (blackList.contains(strBlack[1])) {
                        if (server.deleteStringBlackList(this, strBlack[1])) {
                            blackList.remove(strBlack[1]);
                            sendMsg("Вы извлекли пользователя " + strBlack[1] + " из ЧС");
                        } else {
                            sendMsg("SQL запрос на удаление записи в БД не выполнен. Пользователь не извлечен из ЧС.");
                        }
                    } else {
                        sendMsg("Пользователь " + strBlack[1] + " не найден ЧС");
                    }

                } else {
                    createLogSendMessager(nick, inStr);
                    server.broadcastMsg(this, nick + ": " + inStr);
                }
            }
        }
        close();
    }

    public void close(){
        if(userMsg!=null) {userMsg.close();}
        server.unsubcribe(this);
        try{
            if(socket!=null) {socket.close(); socket=null;}
        }catch (IOException e ){e.printStackTrace();}
    }

    public boolean chackBlackList(String nick){

        return blackList.contains(nick);
    }

    public void sendMsg (String str){
        userMsg.write(str);
    }

    public boolean isClose(){
        return socket.isClosed();
    }

    public String getNick() {
        return nick;
    }

    /**
     * Метод для логирования регистрации и авторизации пользователей
     * @param event - событие регистрации/авторизации
     * @param nick - ник зарегистрированного/авторизованного пользователя
     * */
    public void createLogAddOrRegUser(String event, String nick){
        try {
            logger.setLevel(Level.SEVERE);
            Handler handler = null;
            handler = new FileHandler("logAddOrRegUser.log", 100000,1,true);
            handler.setLevel(Level.ALL);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.log(Level.SEVERE,event + " " + nick);
            logger.removeHandler(handler);
            handler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для логирования сообщений пользователей
     * @param nick - ник авторизованного пользователя
     * @param message - сообщение пользователя
     * */
    public void createLogSendMessager(String nick, String message){
        try {
            logger.setLevel(Level.SEVERE);
            Handler handler = null;
            handler = new FileHandler("logSendMessage.log", 100000,1,true);
            handler.setLevel(Level.ALL);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.log(Level.SEVERE,nick + " " + message);
            logger.removeHandler(handler);
            handler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
