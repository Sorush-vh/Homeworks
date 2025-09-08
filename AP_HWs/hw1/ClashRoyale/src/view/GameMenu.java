package view;

import model.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

import controller.GameMenuController;

public class GameMenu extends Menu {
    public static ArrayList<BattleCard>[][] mapIndexes;


    static{
        mapIndexes=new ArrayList[15][3];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 3; j++) {
                mapIndexes[i][j]=new ArrayList<BattleCard>();
            }
        }
    }


    public static void runGame(User host,User guest, Scanner scanner, int turnCount){

        setStartOfGame(turnCount, host, guest);
        String input,output="";

        while (GameMenuController.turnsCount>0) {
            //order execution
            while (true) {
                input=scanner.nextLine();
                output=GameMenuController.handleOrder(input);
                System.out.print(output);
                output="";
                if(input.equals("next turn"))
                break;
            }
            
            //handling the process of changing turns
            GameMenuController.handleEndOfTurn();

            if( !( (GameMenuController.turnsCount==0 || GameMenuController.isGameFinished()) 
                && GameMenuController.activeUser.getUsername().equals(host.getUsername()) ))

             System.out.println("Player "+GameMenuController.activeUser.getUsername()+" is now playing!");

            else{
                GameMenuController.handlePointsAndWinner();
                break;
            }
        }

        setEndOfGame(host, guest);
        MainMenu.run(scanner);
    }


    private static void setStartOfGame(int turnCount,User host, User guest){
        GameMenuController.turnsCount=turnCount;
        GameMenuController.host=host;
        GameMenuController.guest=guest;
        GameMenuController.setPlayersCastles();

        GameMenuController.playersCardDeployments=1;
        GameMenuController.playersTroopMovements=3;

        GameMenuController.activeUser=host;
        GameMenuController.opponentUser=guest;
    }

    private static void setEndOfGame(User host, User guest){
        controller.Server.updateLeaderboard();
        host.getCastles().clear();
        guest.getCastles().clear();
        for (int j = 0; j < 15; j++) {
            for (int j2 = 0; j2 < 3; j2++) {
                mapIndexes[j][j2].clear();
            }
        }
    }
}
