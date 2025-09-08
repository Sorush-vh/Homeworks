package view;

import model.*;
import controller.Server;
import controller.ProfileMenuController;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu extends Menu {
    
    private static boolean keepGoing=true;



    public static void run(Scanner scanner){
        
        String changePasswordRegex="change password old password (?<oldPassword>.+) new password (?<newPassword>.+)";
        String removeFromDeckRegex="remove from battle deck (?<cardName>.+)";
        String addToDeckRegex="add to battle deck (?<cardName>.+)";

        String input,output="";

        while (keepGoing) {
            input=scanner.nextLine();

            Matcher changePasswordMatcher=Commands.giveTheMatcherStraightUp(input, changePasswordRegex);
            Matcher removeFromDeckMatcher=Commands.giveTheMatcherStraightUp(input, removeFromDeckRegex);
            Matcher addToDeckMatcher=Commands.giveTheMatcherStraightUp(input, addToDeckRegex);

            if(input.equals("back")){
                output="Entered main menu!\n";
                keepGoing=false;
            }
            else if(input.equals(showCurrentMenuFormat)){
                output="Profile Menu\n";
            }
            else if(input.equals("Info")){
                output=ProfileMenuController.tellCurrentUsersInfo();
            }
            else if(input.equals("show battle deck")){
                output=ProfileMenuController.showDeck();
            }
            else if(changePasswordMatcher.matches()){
                String oldPassword=changePasswordMatcher.group("oldPassword");
                String newPassword=changePasswordMatcher.group("newPassword");
                output=ProfileMenuController.changePassword(oldPassword, newPassword);
            }
            else if(removeFromDeckMatcher.matches()){
                String cardName=removeFromDeckMatcher.group("cardName");
                output=ProfileMenuController.removeFromDeck(cardName);
            }
            else if(addToDeckMatcher.matches()){
                String cardName=addToDeckMatcher.group("cardName");
                output=ProfileMenuController.addToDeck(cardName);
            }
            else output="Invalid command!\n";

            System.out.print(output);
            output="";
        }
        keepGoing=true;
        MainMenu.run(scanner);
    }


 
}
