package model;
public class User {
    private String username;
    private String password;
    
    public User(String username, String password){
        this.username=username;
        this.password=password;
        Admin.addUser(this);
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public void resetPassword(String newPassword){
        this.password=newPassword;
    }

}
