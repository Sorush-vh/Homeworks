package controller;
import java.util.regex.Matcher;

import model.*;
import view.Menu;
import view.LoginMenu;

public class LoginMenuController {

    public static String login(String username, String password){
        User targetUser= Admin.getUserByUsername(username);

        if(targetUser==null) return "login failed: username not found\n";

        if(!targetUser.getPassword().equals(password)) return "login failed: incorrect password\n";

        
        LoginMenu.targetUser=targetUser;
        LoginMenu.keepGoing=false;
        return "login successful\n";
    }

    public static String registerCustomer(String username,String password){
        if(!Menu.isUsernameOrPasswordFormatValid(username) || !doesUsernameHaveLetters(username)) return "register failed: invalid username format\n";

        if(Admin.getUserByUsername(username) != null) return "register failed: username already exists\n";

        if(!Menu.isUsernameOrPasswordFormatValid(password)) return "register failed: invalid password format\n";

        if(!Menu.isPasswordStrong(password)) return "register failed: weak password\n";

        Customer temp=new Customer(username, password);
        return "register successful\n";
    }

    public static String changePassword(String targetUsername, String oldPassword, String newPassword){
        User targetUser= Admin.getUserByUsername(targetUsername);

        if(targetUser==null) return "password change failed: username not found\n";

        if(!targetUser.getPassword().equals(oldPassword)) return "password change failed: incorrect password\n";

        if(!Menu.isUsernameOrPasswordFormatValid(newPassword)) return "password change failed: invalid new password\n";

        if(!Menu.isPasswordStrong(newPassword)) return "password change failed: weak new password\n";

        targetUser.resetPassword(newPassword);
        return "password change successful\n";

    }

    public static String removeAccount(String username, String password){
        User targetUser= Admin.getUserByUsername(username);

        if(targetUser==null) return "remove account failed: username not found\n";

        if(!targetUser.getPassword().equals(password)) return "remove account failed: incorrect password\n";

        Admin.removeUserByUsername(username);

        return "remove account successful\n";
    }

    private static boolean doesUsernameHaveLetters(String username){
        String regex="[a-zA-Z]";
        Matcher matcher=Commands.giveTheMatcherStraightUp(username, regex);
        if(!matcher.find()) return false;
        return true;
    }
}
