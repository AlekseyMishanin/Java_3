package lesson_6.DOP_DZ.sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.DataOutputStream;
import java.io.IOException;


public class DialogWindow {

    @FXML
    VBox vboxPw;

    @FXML
    TextField textFieldPw;

    @FXML
    ScrollPane scrollpanePw;

    private String name;
    private Controller parent = null;
    DataOutputStream out = null;

    public void init(){
        this.name=parent.clientList.getSelectionModel().getSelectedItem();

        try {
            out = new DataOutputStream(parent.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLabelForChat(String str){
        vboxPw.heightProperty().addListener((observable)->scrollpanePw.setVvalue(1D));
        Label label = new Label(str);
        Text theText = new Text(label.getText());
        theText.setFont(label.getFont());
        double width = theText.getBoundsInLocal().getWidth(); //замеряем ширину текста
        label.setMaxWidth(Math.min(width, 180)); //выбираем минимальное значение ширины
        label.setWrapText(true);
        Platform.runLater(() -> vboxPw.getChildren().add(label)); //добавление в vbox нового объекта
    }

    public void sendWsgPw(){
        try{
            out.writeUTF("/w " + name + " " + textFieldPw.getText());
            textFieldPw.clear();
            textFieldPw.requestFocus();
        } catch (IOException e){e.printStackTrace();}
    }


    public void setParent(Controller parent){
        this.parent=parent;
    }

}
