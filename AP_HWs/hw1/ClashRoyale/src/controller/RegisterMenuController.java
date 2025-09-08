package controller;

import model.*;
import view.RegisterMenu;
import controller.Server;
import view.Menu;

public class RegisterMenuController {
    

    public static String register(String username, String password){

        if(!Menu.isUsernameValid(username)) return "Incorrect format for username!\n";

        if(!Menu.isPasswordFormatValid(password)) return "Incorrect format for password!\n";

        if(Server.getUserByUsername(username) != null) return "Username already exists!\n";

        User tempUser=new User(username, password);
        setUserStartingTroops(tempUser);

        return "User "+username+" created successfully!\n";
    }

    public static String login(String username, String password){
        if(!Menu.isUsernameValid(username)) return "Incorrect format for username!\n";

        if(!Menu.isPasswordFormatValid(password)) return "Incorrect format for password!\n";

        User targetUser=Server.getUserByUsername(username);
        if(targetUser==null) return "Username doesn't exist!\n";

        if(!targetUser.getPassword().equals(password)) return "Password is incorrect!\n";

        Server.setCurrentUser(targetUser);
        RegisterMenu.keepGoing=false;

        return "User "+username+" logged in!\n";
    }

    private static void setUserStartingTroops(User user){
        BattleCard barbarian=Troops.makeBarbarian();
        Spells fireball=Spells.makeFireball();
        user.getAcquiredCards().add(fireball);
        user.getAcquiredCards().add(barbarian);

        user.getDeck().add(fireball);
        user.getDeck().add(barbarian);
    }
}
