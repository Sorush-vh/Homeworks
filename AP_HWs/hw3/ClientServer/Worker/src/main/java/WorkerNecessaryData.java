import java.util.ArrayList;

public class WorkerNecessaryData {
    private  String id;
    private  Boolean isActive=true;
    private final int MAX_TASK_NUMBER;
    private final String remoteAddress;

    public WorkerNecessaryData(String id, Boolean isActive, int MAX_TASK_NUMBER, String remoteAddress) {
        this.id = id;
        this.isActive = isActive;
        this.MAX_TASK_NUMBER = MAX_TASK_NUMBER;
        this.remoteAddress=remoteAddress;
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

    public void setActive(Boolean active) {
        isActive = active;
    }
}
