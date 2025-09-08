package view;
import java.util.Scanner;
import java.util.regex.Matcher;
import model.*;
import controller.AdminMenuController;

public class AdminMenu extends Menu {
    
    private static boolean keepGoing=true;

    public static void run(Scanner scanner){
        String addRestaurantRegex="\\s*add\\s+restaurant\\s+(?<name>[^\\s]+)\\s+(?<password>[^\\s]+)\\s+(?<type>[^\\s]+)\\s*";
        String showRestaurantRegex="\\s*show\\s+restaurant"+"(\\s+\\-t\\s+(?<type>[^\\s]+))?"+"\\s*";
        String removeRestaurantRegex="\\s*remove\\s+restaurant\\s+(?<name>[^\\s]+)\\s*";
        String setDiscountRegex="\\s*set\\s+discount\\s+(?<username>[^\\s]+)\\s+(?<amount>\\-?[0-9]+)\\s+(?<code>[^\\s]+)\\s*";
        String showDiscountRegex="\\s*show\\s+discounts\\s*";

        String input;
        String output="";

        while (keepGoing) {
            input=scanner.nextLine();

            Matcher addRestaurantMatcher=Commands.giveTheMatcherStraightUp(input, addRestaurantRegex);
            Matcher showRestaurantMatcher=Commands.giveTheMatcherStraightUp(input, showRestaurantRegex);
            Matcher removeRestaurantMatcher=Commands.giveTheMatcherStraightUp(input, removeRestaurantRegex);
            Matcher setDiscountMatcher=Commands.giveTheMatcherStraightUp(input, setDiscountRegex);
            Matcher showDiscountMatcher=Commands.giveTheMatcherStraightUp(input, showDiscountRegex);
            Matcher showMenuMatcher=Commands.giveTheMatcherStraightUp(input, showMenuregex);
            Matcher logoutMatcher=Commands.giveTheMatcherStraightUp(input, logoutRegex);

            if(addRestaurantMatcher.matches()){
                String name=addRestaurantMatcher.group("name");
                String password=addRestaurantMatcher.group("password");
                String type=addRestaurantMatcher.group("type");
                output=AdminMenuController.addRestaurant(name, password, type);
            }
            else if(showMenuMatcher.matches()){
                output="Snappfood admin menu"+"\n";
            }
            else if(logoutMatcher.matches()){
                keepGoing=false;
                output="";
            }
            else if(showRestaurantMatcher.matches()){
                String type=showRestaurantMatcher.group("type");
                output=AdminMenuController.showRestaurants(type);
            }
            else if(removeRestaurantMatcher.matches()){
                String name=removeRestaurantMatcher.group("name");
                output=AdminMenuController.removeRestaurant(name);
            }
            else if(showDiscountMatcher.matches()){
                output=AdminMenuController.showDiscounts();
            }
            else if(setDiscountMatcher.matches()){
                String username=setDiscountMatcher.group("username");
                String amount=setDiscountMatcher.group("amount");
                String code=setDiscountMatcher.group("code");
                output=AdminMenuController.setDiscount(username, Integer.parseInt(amount), code);
            }
            else output="invalid command!\n";

            System.out.print(output);
            output="";
        }
        keepGoing=true;
        MainMenu.setUser(null);
         LoginMenu.run(scanner);
    }


}
