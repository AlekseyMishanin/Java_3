package lesson_6.DOP_DZ.sample;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller  {

    @FXML
    ScrollPane scrollpane;
    @FXML
    VBox vbox;

    @FXML
    TextField textField;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button btnAuth;

    @FXML
    HBox loginpassBox;

    @FXML
    HBox chatBox;

    @FXML
    Button btn1;

    @FXML
    MenuItem menuList;

    @FXML
    ListView<String> clientList;

    @FXML
    TextField regLogField;

    @FXML
    TextField regNickField;

    @FXML
    PasswordField regPassField;

    @FXML
    VBox regBox;

    @FXML
    BorderPane borderpane;

    @FXML
    MenuItem menuAuthorization;

    @FXML
    MenuItem menuRegistration;

    private boolean isAvtorize = false;
    private String nick = null;
    Socket socket;
    DataInputStream in= null;
    DataOutputStream out = null;
    private InetAddress ip = null;
    final int PORT = 1501;
    DialogWindow children = null;


    public void setAvtorize(boolean isAvtorize){
        this.isAvtorize = isAvtorize;
        if(!isAvtorize){
            chatBox.setVisible(false);
            chatBox.setManaged(false);
            loginpassBox.setVisible(true);
            loginpassBox.setManaged(true);
            menuAuthorization.setVisible(true);
            menuRegistration.setVisible(true);
        } else {
            chatBox.setVisible(true);
            chatBox.setManaged(true);
            loginpassBox.setVisible(false);
            loginpassBox.setManaged(false);
            menuAuthorization.setVisible(false);
            menuRegistration.setVisible(false);
            regBox.setVisible(false);
            regBox.setManaged(false);
        }
    }

    public void connect(){

        try{
            ip = InetAddress.getLocalHost();
            socket = new Socket(ip, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            vbox.heightProperty().addListener((observable)->scrollpane.setVvalue(1D)); //автоматическая прокрутка scrollpane при добавлении нового объекта в vbox
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Timer timer = new Timer();              //создаем таймер
                        TimerTask task = new TimerTask() {      //создаем задание
                            @Override
                            public void run() {
                                if(!isAvtorize){                    //если пользователь не авторизован
                                    try {
                                        out.writeUTF("/end");   //оправляем на сервер сообщение для разрыва соединения
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        timer.schedule(task,120000);        //просим таймер выполнить задание один раз по истечении 120секунт

                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("/serverclose")){
                                createLabelForChat("Соединение с сервером разорвано.");
                                timer.cancel(); timer=null;
                                close();
                                return;
                            }
                            if(str.startsWith("/authok ")){
                                setAvtorize(true);
                                String[] tokens = str.split(" ",2);
                                nick=tokens[1];
                                break;
                            } else {
                                createLabelForChat(str);
                            }
                        }
                        while (true) {
                            String str = in.readUTF();
                            if(str.startsWith(".") && children!=null){  //если получено личное сообщение и если существуе дочернее окно (для личных ссобщение)то
                                children.createLabelForChat(str);       //рисуем в дочернем окне текс сообщения
                            }
                            if (str.equals("/serverclose")){
                                timer.cancel(); timer=null;
                                break;
                            }
                            else if (str.startsWith("/clientlist ")){
                                String[] tokens = str.split(" ");
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        clientList.getItems().clear();
                                        for (int i = 1; i < tokens.length; i++) {
                                            clientList.getItems().add(tokens[i]);
                                        }
                                    }
                                });
                            } else {
                                createLabelForChat(str);
                            }
                        }
                    }catch (IOException e){e.printStackTrace();}
                    finally {
                        close();
                    }
                }
            });
            th.setDaemon(true);
            th.start();
        } catch (IOException e){e.printStackTrace();}
    }

    public void close (){
        try {
            if(in!=null) {in.close(); in=null;}
            if(out!=null) {out.close(); out=null;}
            if(socket!=null) {socket.close(); socket=null;}
        } catch (IOException e){e.printStackTrace();}
        setAvtorize(false);
    }

    public void closeChat(){
        if(isAvtorize){
            try {
                out.writeUTF("/end");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Platform.exit();
    }

    public void sendWsg(){
        try{
            out.writeUTF(textField.getText());

            textField.clear();
            textField.requestFocus();
        } catch (IOException e){e.printStackTrace();}
    }

    public void playAnimation(Node node){
        //Creating a rotate transition
        RotateTransition rotateTransition = new RotateTransition();

        //Setting the duration for the transition
        rotateTransition.setDuration(Duration.millis(500));

        //Setting the node for the transition
        rotateTransition.setNode(node);

        //Setting the angle of the rotation
        rotateTransition.setByAngle(360);

        //Setting the cycle count for the transition
        rotateTransition.setCycleCount(1);

        //Setting auto reverse value to false
        rotateTransition.setAutoReverse(false);

        //Playing the animation
        rotateTransition.play();
    }


    public void tryToAuth(ActionEvent actionEvent) {
        if(socket==null||socket.isClosed()){
            connect();
        }
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //метод показывает или скрывает список авторизованных пользователей
    public void seeList(ActionEvent actionEvent) {
        if(!clientList.isVisible()&&isAvtorize){
            menuList.setText("Скрыть онлайн");
            clientList.setVisible(true);
            clientList.setManaged(true);
        } else {
            menuList.setText("Показать онлайн");
            clientList.setVisible(false);
            clientList.setManaged(false);
        }
    }

    //метод показывает или скрывает репистрационную панель
    public void seeRegPane(ActionEvent actionEvent) {
        if(!regBox.isVisible()&&!isAvtorize){
            regBox.setVisible(true);
            regBox.setManaged(true);
        } else {
            regBox.setVisible(false);
            regBox.setManaged(false);
        }
    }

    //метод показывает или скрывает панель авторизации
    public void seeAuthPane(ActionEvent actionEvent) {
        if(!!isAvtorize){
            loginpassBox.setVisible(true);
            loginpassBox.setManaged(true);
        } else {
            loginpassBox.setVisible(false);
            loginpassBox.setManaged(false);
        }
    }

    //метод оформляет полученное сообщение и добавляет его в vbox
    private void createLabelForChat(String str){
        Label label = new Label(str);
        Text theText = new Text(label.getText());
        theText.setFont(label.getFont());
        double width = theText.getBoundsInLocal().getWidth(); //замеряем ширину текста
        label.setMaxWidth(Math.min(width, 180)); //выбираем минимальное значение ширины
        label.setWrapText(true);
        if (str.startsWith(".")) {
            label.setStyle("-fx-text-fill: firebrick");
        } //если сообщение личное, меняем цвет текста
        playAnimation(label);
        Platform.runLater(() -> vbox.getChildren().add(label)); //добавление в vbox нового объекта
    }

    //метод для регистраниции нового пользователя. Пароль, логин, ник проверяются на валидность.
    public void addNewUser(ActionEvent actionEvent) {
        if(socket==null||socket.isClosed()){
            connect();
        }
        try {
            Pattern patternLoginAndNick = Pattern.compile("^.+$");              //шаблон для проверки логина и пароля на наличие хотя бы 1-го символа
            Pattern patternPassword = Pattern.compile("^[A-Z]+[a-z]+$");        //шаблон проверки пароля на наличие большой и маленькой буквы (латинница)
            Matcher matcherLogin = patternLoginAndNick.matcher(regLogField.getText());
            Matcher matcherNick = patternLoginAndNick.matcher(regNickField.getText());
            Matcher matcherPassword = patternPassword.matcher(regPassField.getText());
            if(!matcherLogin.matches()){
                createLabelForChat("Логин не может быть пустым значением.");
            } else if (!matcherNick.matches()){
                createLabelForChat("Ник не может быть пустым значением.");
            } else if (!matcherPassword.matches()) {
                createLabelForChat("Пароль должен: состоять из букв, начинаться с заглавной буквы.");
            } else {
                out.writeUTF("/addUser " + regLogField.getText() + " " + regPassField.getText() + " " + regNickField.getText());
            }
            regNickField.clear();
            regLogField.clear();
            regPassField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //метод создания диалогового окна под личный чат с выбранным пользователем
    public void createPmChat(ActionEvent actionEvent) {
        String nameTo = clientList.getSelectionModel().getSelectedItem(); //получаем текс выбранной строки
        if(!nameTo.equals(this.nick)){    //сравниваем, чтобы ник получателя не совпадал с ником отправителя
            try {
                FXMLLoader loader = new FXMLLoader(DialogWindow.class.getResource("/lesson_6/DOP_DZ/sample/dialogWindow.fxml")); //получаем экземпялр FXMLLoader на основе fxml
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root, 290, 240);
                stage.setScene(scene);
                stage.setTitle("Чат с " + nameTo);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(clientList.getScene().getWindow());
                stage.show();
                children = loader.getController();  //получаем контроллер дочернего окна
                children.setParent(this);           //передаем дочернему окну ссылку на контроллер родительского окна
                children.init();                    //инициализируем переменные дочернего окна
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
