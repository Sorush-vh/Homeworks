
package controller;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private static ArrayList<User> users;
    private static User currentUser;
    private static HashMap<User, Integer> leaderboard;

    static{
        users= new ArrayList<User>();
        leaderboard= new HashMap<User, Integer>();
    }
    
    public static HashMap<User, Integer> getLeaderboard(){
        return leaderboard;
    }

    public static User GetUserByRank(int rank){
        updateLeaderboard();
        for (User user : users) {
            if(leaderboard.get(user)==rank) return user;
        }
        return null;
    }

    public static int GetUsersRank(User user){
        updateLeaderboard();
        return leaderboard.get(user);
    }

    public static void addUser(User user){
        users.add(user);
        leaderboard.put(user, leaderboard.size());
    }

    public static User getUserByUsername(String username){
        for (User user : users) {
            if(user.getUsername().equals(username)) return user;
        }
        return null;
    }

    public static ArrayList<User> getUsers(){
        return users;
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static void setCurrentUser(User user){
        currentUser=user;
    }

    public static void updateLeaderboard(){
        leaderboard.clear();
        User tempUser,UserUnderCheck;
        int rank=1;

        for (int i = 0; i < users.size(); i++) {
            UserUnderCheck=users.get(i);
            for (int j = 0; j < users.size(); j++) {

                if(j==i) continue;

                tempUser=users.get(j);
                if(IsUserLower(UserUnderCheck, tempUser)) rank++;
            }
            leaderboard.put(UserUnderCheck,rank);
            rank=1;
        }
    }

    private static boolean IsUserLower(User target, User temp){

        if(target.getLevel()<temp.getLevel()) return true;

        else if(target.getLevel()==temp.getLevel()){

                if(target.getExperience()<temp.getExperience()) return true;

                else if(target.getExperience()==temp.getExperience()){
                    if(target.getUsername().compareTo(temp.getUsername()) >0) return true;
                }
            }
            return false;
    }

}
