import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;

public class Worker {
    private final int MAX_TASK_NUMBER;
    private  String id;
    private  Boolean isActive=true;
    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    final Socket socket;


    public Worker(String host, int port) throws UnknownHostException, IOException {
        socket=new Socket(host,port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        System.out.println("enter your id:");
        id=scanner.nextLine();

        System.out.println("enter your maximum task number:");
        MAX_TASK_NUMBER=Integer.parseInt(scanner.nextLine());

        this.isActive=true;
        sendDataToServer();
        System.out.println("You have succesfully registered as a worker in Master server!");
    }
    
    private void sendDataToServer(){
        WorkerNecessaryData workerData=new WorkerNecessaryData(id,true,MAX_TASK_NUMBER,
            getRemoteAddress()) ;
        Gson gson=new Gson();
        Message message=new Message(id, gson.toJson(workerData));
        try {
            dataOutputStream.writeUTF(gson.toJson(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPort(){
        return Integer.toString(socket.getPort());
    }

    public String getRemoteAddress(){
        return socket.getRemoteSocketAddress().toString();
    }

    public int getMAX_TASK_NUMBER() {
        return MAX_TASK_NUMBER;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public Boolean getActive() {
        return isActive;
    }
}
