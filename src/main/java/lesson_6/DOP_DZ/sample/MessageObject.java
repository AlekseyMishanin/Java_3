package lesson_6.DOP_DZ.sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MessageObject {

    private String message = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public MessageObject(Socket socket) {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e ){e.printStackTrace();}
    }


    public String read(){
        try{
            message = in.readUTF();
        }catch (IOException e ){e.printStackTrace();}
        return message;
    }

    public void write(String str){
        try{
            out.writeUTF(str);
        }catch (IOException e ){e.printStackTrace();}

    }



    public void close(){
        try{
            in.close();
        }catch (IOException e ){e.printStackTrace();}
        try{
            out.close();
        }catch (IOException e ){e.printStackTrace();}
    }
}

