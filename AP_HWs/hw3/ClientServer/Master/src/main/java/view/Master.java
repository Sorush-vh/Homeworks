package view;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import controller.MasterOperator;
import model.Task;
import model.WorkerNecessaryData;

public class Master {

        private ArrayList<Task> tasks;
        private ArrayList<Task> tasksOnHold;
        private  ArrayList<WorkerNecessaryData> workers;
        public Master(int port) {
            workers=new ArrayList<>();
            tasks=new ArrayList<>();
            tasksOnHold=new ArrayList<>();
            System.out.println("Server Initialized! ");
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (true){
                    Socket socket = serverSocket.accept();
                    MasterOperator masterOperator = new MasterOperator(socket,this);
                    masterOperator.start();
                }
            } catch (IOException e) {
                //TODO: try to reconnect...
            }
        }

    public ArrayList<Task> getTasksOnHold() {
        return tasksOnHold;
    }

    public Task getTaskByName(String name){
        for (Task task: getTasks()) {
            if(task.getName().equals(name))
                return task;
        }
        return null;
    }

    public WorkerNecessaryData getLeastBusyWorker(){
            if(workers.size()==0) return null;
            WorkerNecessaryData leastBusy=workers.get(0);
            for (WorkerNecessaryData worker:getWorkers()) {
                if(leastBusy.getCapacity()<worker.getCapacity() && worker.getActive())
                    leastBusy=worker;
                if(!leastBusy.getActive() && worker.getActive())
                    leastBusy=worker;
            }
            if(!leastBusy.getActive()) return null;
            return leastBusy;
        }

        public WorkerNecessaryData getWorkerById(String id){
            for (WorkerNecessaryData worker: workers)
                if(worker.getId().equals(id))
                    return  worker;
            return null;
        }

        public ArrayList<WorkerNecessaryData> getWorkers() {
            return workers;
        }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void startNextTask(){
        Task nextTask=tasksOnHold.get(0);
        WorkerNecessaryData bestWorker=getLeastBusyWorker();
        bestWorker.addTask(nextTask);
        nextTask.setRunning(true);
        nextTask.setPendingWorker(bestWorker);
        tasksOnHold.remove(nextTask);
    }

    public void updateWaitingNodes(){
            if(tasksOnHold.size()==0) return;
            if( getLeastBusyWorker()==null) return;
            if(getLeastBusyWorker().isFill()) return;
            startNextTask();
            updateWaitingNodes();
    }

}
