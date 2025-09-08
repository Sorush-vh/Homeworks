package model;

public class Task {
    private String name;
    private  Boolean isRunning;

    private WorkerNecessaryData pendingWorker;

    public Task(String name, Boolean isRunning) {
        this.name = name;
        this.isRunning = isRunning;
    }

    public void setPendingWorker(WorkerNecessaryData pendingWorker) {
        this.pendingWorker = pendingWorker;
    }

    public WorkerNecessaryData getPendingWorker() {
        return pendingWorker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public Boolean getRunning() {
        return isRunning;
    }
}
