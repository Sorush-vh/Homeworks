package view;

import model.*;
import controller.Server;
import controller.MainMenuController;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu extends Menu {
    

    private static boolean keepGoing=true;


    public static void run(Scanner scanner){
        String input,output="";

        MainMenuController.nextMenu="";
        MainMenuController.opponent=null;
        MainMenuController.gameTurnCounter=0;

        String startGameRegex="start game turns count (?<turnsCount>\\d+) username (?<username>.+)";

        while (keepGoing) {
            input=scanner.nextLine();
            Matcher startGameMatcher=Commands.giveTheMatcherStraightUp(input, startGameRegex);

            if(input.equals("list of users")){
                output=MainMenuController.showListOfUsers();
            }
            else if(input.equals("scoreboard")){
                output=MainMenuController.showScoreboard();
            }
            else if(input.equals(showCurrentMenuFormat)){
                output="Main Menu\n";
            }
            else if(input.equals("profile menu")){
                output="Entered profile menu!\n";
                MainMenuController.nextMenu="profile";
                keepGoing=false;
            }
            else if(startGameMatcher.matches()){
                String turn=startGameMatcher.group("turnsCount");
                String opponentz=startGameMatcher.group("username");
                output=MainMenuController.startGame(turn, opponentz);
                if(MainMenuController.opponent != null){
                    MainMenuController.nextMenu="game";
                keepGoing=false;
                }
            }
            else if(input.equals("logout")){
                output="User "+Server.getCurrentUser().getUsername()+" logged out successfully!\n";
                Server.setCurrentUser(null);
                MainMenuController.nextMenu="register";
                keepGoing=false;
            }
            else if(input.equals("shop menu")){
                output="Entered shop menu!\n";
                MainMenuController.nextMenu="shop";
                keepGoing=false;
            }
            else output="Invalid command!\n";

            System.out.print(output);
            output="";
        }
        keepGoing=true;
        MainMenuController.runNextMenuAccordingly(scanner);
    }



}
