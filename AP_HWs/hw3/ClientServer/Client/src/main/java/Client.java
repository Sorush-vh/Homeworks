import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private Boolean keepGoing=true;

    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    final Socket socket;


    public Client(String host, int port) throws IOException {
       socket=new Socket(host,port);
       dataInputStream = new DataInputStream(socket.getInputStream());
       dataOutputStream = new DataOutputStream(socket.getOutputStream());
       Scanner scanner = new Scanner(System.in);
        System.out.println("your contact started with master!");
       while (keepGoing)
           handleOrders(scanner);
    }

    private  void handleOrders(Scanner scanner) throws IOException {
        String input=scanner.nextLine();
        Message message= new Message(null,input);
        Gson gson=new Gson();
        dataOutputStream.writeUTF(gson.toJson(message));
        readResponse();
    }

    private void readResponse(){
        Message message;
        try {
            message=new Gson().fromJson(dataInputStream.readUTF(),Message.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message.getBody());
        if(message.getBody().equals("finished interaction with Master"))
            keepGoing=false;
    }
}
