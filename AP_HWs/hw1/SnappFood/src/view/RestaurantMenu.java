package view;
import model.*;
import controller.RestaurantMenuController;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;


public class RestaurantMenu extends NormalMenu {
    private static Restaurant currentRestaurant;
    private static boolean keepGoing=true;


    public static void run(Scanner scanner, Restaurant restaurant){
        setCurrentRestaurant(restaurant);
        RestaurantMenuController.setCurrentRestaurant(restaurant);
        
        String addFoodRegex="\\s*add\\s+food\\s+(?<name>[^\\s]+)\\s+(?<category>[^\\s]+)\\s+(?<price>[^\\s]+)\\s+(?<cost>[^\\s]+)\\s*";
        String removeFoodRegex="\\s*remove\\s+food\\s+(?<name>[^\\s]+)\\s*";


        String input;
        String output="";
        while (keepGoing) {
            input=scanner.nextLine();

            Matcher logoutMatcher=Commands.giveTheMatcherStraightUp(input,logoutRegex);
            Matcher showMenuTypeMatcher=Commands.giveTheMatcherStraightUp(input, showMenuregex);
            Matcher showBalanceMatcher=Commands.giveTheMatcherStraightUp(input, showBalanceregex);
            Matcher addBalanceMatcher=Commands.giveTheMatcherStraightUp(input, chargeBalanceRegex);
            Matcher addFoodMatcher=Commands.giveTheMatcherStraightUp(input, addFoodRegex);
            Matcher removeFoodMatcher=Commands.giveTheMatcherStraightUp(input, removeFoodRegex);

            if(logoutMatcher.matches()){
                output="";
                keepGoing=false;
            }
            else if(showMenuTypeMatcher.matches()){
                output="restaurant admin menu"+"\n";
            }
            else if(showBalanceMatcher.matches()){
                output=showBalance(currentRestaurant);
            }
            else if(addBalanceMatcher.matches()){
                int charge=Integer.parseInt(addBalanceMatcher.group("amount")); 
                output=chargeBalance(currentRestaurant,charge);
            }
            else if(addFoodMatcher.matches()){
                String name=addFoodMatcher.group("name");
                String category=addFoodMatcher.group("category");
                int price= Integer.parseInt(addFoodMatcher.group("price"));
                int cost=Integer.parseInt(addFoodMatcher.group("cost"));
                output=RestaurantMenuController.addFoodToMenu(name, category, price, cost);
            }
            else if(removeFoodMatcher.matches()){
                String name=removeFoodMatcher.group("name");
                output=RestaurantMenuController.removeFoodFromMenu(name);
            }
            else output="invalid command!\n";

            System.out.print(output);
            output="";
        }
        keepGoing=true;
        setCurrentRestaurant(null);
        MainMenu.setUser(null);
        LoginMenu.run(scanner);
    }

    private static void setCurrentRestaurant(Restaurant restaurant){
        currentRestaurant=restaurant;
    }

}
