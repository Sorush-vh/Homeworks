package model;
public class NormalUsers extends User {
    private int balance;

    public NormalUsers(String username, String password){
        super(username, password);
    }

    public void addBalance(int addedValue){
        this.balance+=addedValue;
        //To Do: handle invalid values
    }

    public int getBalance(){
        return this.balance;
    }
}
