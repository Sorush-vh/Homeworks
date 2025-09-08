package controller;
import view.Menu;
import model.*;
import view.MainMenu;

import java.util.regex.Matcher;



public class MainMenuController {
    
    private static User currentUser=null;

    public static String enterMenu(String menuName){

    
        String menuRegex="(\\s+Snappfood\\s+(?<admin>admin)\\s+menu\\s*)|(\\s+(?<customer>customer)\\s+menu\\s*)|(\\s+(?<restaurant>restaurant)\\s+admin\\s+menu\\s*)";
        Matcher menuMatcher=Commands.giveTheMatcherStraightUp(menuName, menuRegex);
        if(!menuMatcher.matches()) return "enter menu failed: invalid menu name\n";

        String trimmedMenuInput=findFilledMenuType(menuMatcher);
        if(!getCurrentUsersMenuType().equals(trimmedMenuInput)) return "enter menu failed: access denied\n";


        MainMenu.keepGoing=false;
        return "enter menu successful: You are in the "+trimmedMenuInput+"!\n";
    } 

    private static String getCurrentUsersMenuType(){
        if(Admin.getAdmin().getUsername().equals(currentUser.getUsername())) return "Snappfood admin menu";
        if(Admin.getRestaurantByName(currentUser.getUsername()) != null) return "restaurant admin menu";
        return "customer menu";
    }

    private static String  findFilledMenuType(Matcher matcher){
        if(matcher.group("admin")!=null) return "Snappfood admin menu";
        else if(matcher.group("customer")!=null) return "customer menu";
        else return "restaurant admin menu";
    }

    public static void setUser(User user){
        currentUser=user;
    }

}
