package controller;

import com.google.gson.Gson;
import model.Message;
import model.Task;
import model.WorkerNecessaryData;
import view.ClientCommands;
import view.Master;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;

public class MasterOperator extends  Thread {

    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    private Socket socket;
    private Master master;

    private Boolean keepGoing=true;
    public MasterOperator(Socket socket, Master master) throws IOException {
        this.socket=socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.master=master;
    }

    @Override
    public synchronized void run() {
        Message message;
        while (keepGoing){
            message=getDataFromClient();
            handleClientInput(message);
        }
    }

    private void handleClientInput(Message message){

        if(message.getSenderId() != null){
            addWorkerToMaster(message.getBody());
            keepGoing=false;
        }

        String input=message.getBody().toString();
        Matcher matcher;

        if(input.equals("exit")) {
            keepGoing=false;
            sendMessageToClient("finished interaction with Master");
        }

        if( (matcher= ClientCommands.getMatcher(input, ClientCommands.CREATE_TASK)) != null)
            createTask(matcher);
        else if((matcher= ClientCommands.getMatcher(input, ClientCommands.GET_NODES)) != null)
            showWorkers();
        else if((matcher= ClientCommands.getMatcher(input, ClientCommands.DELETE_TASK)) != null)
            deleteTask(matcher);
        else if((matcher= ClientCommands.getMatcher(input, ClientCommands.GET_TASKS)) != null)
            getTasks();
        else if((matcher= ClientCommands.getMatcher(input, ClientCommands.CORDON_NODES)) != null)
            cordonNode(matcher);
        else if((matcher= ClientCommands.getMatcher(input, ClientCommands.UNCORDON_NODES)) != null)
            uncordonNode(matcher);
        else sendMessageToClient("Wrong command!");

    }
    private void cordonNode(Matcher matcher){
        String workerId=matcher.group("nodeName");
        WorkerNecessaryData targetNode=master.getWorkerById(workerId);

        if(targetNode==null){
            sendMessageToClient("Error: no such node exists!");
            return;
        }
        targetNode.setActive(false);
        for (Task task:targetNode.getTasks()) {
            task.setRunning(false);
            task.setPendingWorker(null);
            master.getTasksOnHold().add(task);
        }
        targetNode.getTasks().clear();
        master.updateWaitingNodes();
        sendMessageToClient("node deactivated succesfully!");
    }

    private void uncordonNode(Matcher matcher){
        String workerId=matcher.group("nodeName");
        WorkerNecessaryData targetNode=master.getWorkerById(workerId);
        if(targetNode==null){
            sendMessageToClient("Error: no such node exists!");
            return;
        }
        targetNode.setActive(true);
        master.updateWaitingNodes();
        sendMessageToClient("node activated succesfully!");
    }
    private void getTasks(){
        String output="";
        int i=1;
        for (Task task: master.getTasks()) {
            output=output.concat("Task"+(i++)+":\n");
            output=output.concat("name: "+task.getName()+"\n");
            output=output.concat("running status: "+task.getRunning()+"\n");
            if(task.getRunning())
                output=output.concat("node in charge: "+task.getPendingWorker().getId()+"\n");
        }
        sendMessageToClient(output);
    }

    private void deleteTask(Matcher matcher){
        String taskName=matcher.group("taskName");
        Task task=master.getTaskByName(taskName);
        if(task==null){
            sendMessageToClient("Error: no such task with this name!");
            return;
        }
        master.getTasks().remove(task);
        if(task.getRunning()){
            task.getPendingWorker().getTasks().remove(task);
            task.setPendingWorker(null);
            master.updateWaitingNodes();
        }
        else master.getTasksOnHold().remove(task);
        sendMessageToClient("Task removed succesfully!");
    }

    private void sendMessageToClient(String messageBody){
        Message message=new Message(null,messageBody);
        try {
             dataOutputStream.writeUTF(new Gson().toJson(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private  void addWorkerToMaster(Object workerObject){
        WorkerNecessaryData workerData=new Gson().fromJson(workerObject.toString(),WorkerNecessaryData.class);
        master.getWorkers().add(workerData);
        workerData.initializeTaskList();
        System.out.println(master.getWorkers().size());
    }

    private void createTask(Matcher matcher){
        String targetWorkerId=matcher.group("workerId");
        String taskName=matcher.group("name");
        if(master.getTaskByName(taskName) != null){
            sendMessageToClient("Error: task name already in use!");
            return;
        }
        Task task=new Task(taskName,false);
        master.getTasks().add(task);
        master.getTasksOnHold().add(task);

        String output="Task created succesfully and is on hold by default\n";

        if(targetWorkerId!= null)
            handleTaskByTarget(master.getWorkerById(targetWorkerId),task);

        else{
            WorkerNecessaryData bestWorker=master.getLeastBusyWorker();
            if(bestWorker==null){
                output=output.concat("Error handling the task: there are currently no workers to handle task!");
                sendMessageToClient(output);
                return;
            }
            if(bestWorker.isFill()){
                output=output.concat("Error handling the task: there are currently no free workers remaining!");
                sendMessageToClient(output);
                return;
            }
            bestWorker.addTask(task);
            task.setRunning(true);
            task.setPendingWorker(bestWorker);
            master.getTasksOnHold().remove(task);

            output=output.concat("Task handled succesfully");
            sendMessageToClient(output);
        }
    }

    private void handleTaskByTarget(WorkerNecessaryData worker, Task task){
        if(worker==null) {
            sendMessageToClient("Task Handling Error: Invalid Id for Worker node!");
            master.updateWaitingNodes();
            return;
        }
        if(worker.isFill()){
            sendMessageToClient("Task Handling Error: the target workers task capacity is already full!");
            master.updateWaitingNodes();
            return;
        }
        if(!worker.getActive()){
            sendMessageToClient("Task Handling Error: target worker is inactive!");
            master.updateWaitingNodes();
            return;
        }
        worker.addTask(task);
        task.setRunning(true);
        master.getTasksOnHold().remove(task);
        task.setPendingWorker(worker);
        sendMessageToClient("Worker accepted the task succesfully!");
    }

    private void showWorkers(){
        String output="";
        int j=1;
        for (WorkerNecessaryData worker : master.getWorkers()) {
            output=output.concat("Node-"+(j++)+":\n");
            output=output.concat("Id: "+worker.getId()+"\n");
            output=output.concat("Remote Address: "+worker.getRemoteAddress()+"\n");
            output=output.concat("Tasks:\n");
            int i=1;
            for (Task task: worker.getTasks())
                output=output.concat("task "+(i++)+": "+task.getName()+"\n");
        }
        sendMessageToClient(output);
    }

    private Message getDataFromClient(){
        String input=null;
        try {
           input  = dataInputStream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gson gson=new Gson();
        return gson.fromJson(input, Message.class);
    }
}
