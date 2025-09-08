package model;

import java.util.ArrayList;

public class WorkerNecessaryData {
    private  String id;
    private  Boolean isActive=true;
    private final int MAX_TASK_NUMBER;
    private final String remoteAddress;

    private ArrayList<Task> tasks;

    public WorkerNecessaryData(String id, Boolean isActive, int MAX_TASK_NUMBER, String remoteAddress) {
        this.id = id;
        this.isActive = isActive;
        this.MAX_TASK_NUMBER = MAX_TASK_NUMBER;
        this.remoteAddress=remoteAddress;
    }

    public Boolean isFill(){
        if(tasks.size()==MAX_TASK_NUMBER)
            return true;
        return false;
    }

    public double getCapacity(){
        return ((double) 1-(double) tasks.size()/MAX_TASK_NUMBER)*100;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void initializeTaskList(){
        tasks=new ArrayList<>();
    }
    public String getId() {
        return id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public int getMAX_TASK_NUMBER() {
        return MAX_TASK_NUMBER;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
