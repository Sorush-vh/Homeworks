package controller;

import java.util.Scanner;

import model.*;
import view.*;



public class MainMenuController {



    public static String nextMenu;
    public static User opponent;
    public static int gameTurnCounter;

    public static String startGame(String turnsCount,  String opponentName){
        int turnsInNumber=Integer.parseInt(turnsCount);

        if(turnsInNumber<5 || turnsInNumber>30) return "Invalid turns count!\n";

        if(!Menu.isUsernameValid(opponentName)) return "Incorrect format for username!\n";

        if(Server.getUserByUsername(opponentName)==null) return "Username doesn't exist!\n";

        opponent=Server.getUserByUsername(opponentName);

        gameTurnCounter=turnsInNumber;
        return "Battle started with user "+opponentName+"\n";
    }

    public static String showListOfUsers(){
        String output="";
        int index=1;

        for (User user : Server.getUsers()) {
            output=output.concat("user "+index+": ");
            output=output.concat(user.getUsername());
            output=output.concat("\n");

            index++;
        }

        return output;
    }

    public static String showScoreboard(){
        //<rank>- username: <username> level: <level> experience: <experience>
        String output="";
        User indexUser;
        int validIteration;

        if(Server.getUsers().size()>5) validIteration=5;
        else validIteration=Server.getUsers().size();

        for (int i = 1; i < validIteration+1; i++) {

            indexUser=Server.GetUserByRank(i);
            output=output.concat(""+i+"- ");
            output=output.concat("username: "+indexUser.getUsername()+" ");
            output=output.concat("level: "+indexUser.getLevel()+" ");
            output=output.concat("experience: "+indexUser.getExperience());
            output=output.concat("\n");
        }

        return output;
    }

    public static void runNextMenuAccordingly(Scanner scanner){

        if(nextMenu.equals("register")){
            Server.setCurrentUser(null);
            RegisterMenu.run(scanner);
        }
        else if(nextMenu.equals("shop")){
            ShopMenu.run(scanner);
        }
        else if(nextMenu.equals("profile")){
            ProfileMenu.run(scanner);
        }
        else if(nextMenu.equals("game")){
            GameMenu.runGame(Server.getCurrentUser(), opponent, scanner, gameTurnCounter);
        }
    }

}
