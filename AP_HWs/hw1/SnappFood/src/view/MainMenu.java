package view;
import java.util.Scanner;
import java.util.regex.Matcher;

import controller.MainMenuController;
import model.*;

public class MainMenu extends Menu {
    
    private static User currentUser=null;
    public static boolean keepGoing=true;


    public static void run(User user, Scanner scanner){
        setUser(user);
        MainMenuController.setUser(user);

        String input;
        String output="";

        String enterMenuRegex="\\s*enter(?<menuType>.+)";

        while (keepGoing) {
            input=scanner.nextLine();

            Matcher enterMenuMatcher=Commands.giveTheMatcherStraightUp(input, enterMenuRegex);
            Matcher showMenuTypeMatcher=Commands.giveTheMatcherStraightUp(input, showMenuregex);
            Matcher logoutMatcher=Commands.giveTheMatcherStraightUp(input, logoutRegex);

            if(enterMenuMatcher.matches()){
                String menuInput=enterMenuMatcher.group("menuType");
                output=MainMenuController.enterMenu(menuInput);
            }
            else if(showMenuTypeMatcher.matches()){
                output="main menu"+"\n";
            }
            else if(logoutMatcher.matches()){
                keepGoing=false;
                currentUser=null;
                output="";
            }
            else output="invalid command!\n";

            System.out.print(output);
        }

        keepGoing=true;

        if(currentUser== null) {
            LoginMenu.run(scanner);
            return ;
        }
        else if(Admin.getAdmin().getUsername().equals(currentUser.getUsername())) {
            AdminMenu.run(scanner);
            return ;
        }
        else if(Admin.getRestaurantByName(currentUser.getUsername()) != null){
             RestaurantMenu.run(scanner, (Restaurant) currentUser);
             return;
        }
        else if(Admin.getUserByUsername(currentUser.getUsername()) != null) {
         CustomerMenu.run(scanner, (Customer) currentUser);
         return ;
        }
 
    }

    public static void setUser(User user){
        currentUser=user;
    }

}
