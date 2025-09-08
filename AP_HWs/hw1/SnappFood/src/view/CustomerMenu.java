package view;
import model.*;
import controller.CostumerMenuController;
import java.util.Scanner;
import java.util.regex.Matcher;

import controller.CostumerMenuController;

public class CustomerMenu extends NormalMenu {
    
    public static Customer currentCustomer;
    private static boolean keepGoing=true;

    public static void run(Scanner scanner, Customer customer){
        setCurrentCustomer(customer);
        CostumerMenuController.setCurrentCustomer(customer);

        String showRestaurantsRegex="\\s*show\\s+restaurant"+ "(\\s+\\-t\\s+(?<type>[^\\s]+))?"+"\\s*";
        String showRestaurantMenuRegex="\\s*show\\s+menu\\s+(?<name>[^\\s]+)"+"(\\s+\\-c\\s+(?<category>[^\\s]+))?"+"\\s*";
        String addToCartRegex="\\s*add\\s+to\\s+cart\\s+(?<restaurantname>[^\\s]+)\\s+(?<foodname>[^\\s]+)"+"(\\s+\\-n\\s+(?<number>\\-?[0-9]+))?"+"\\s*";
        String removeFromCartRegex="\\s*remove\\s+from\\s+cart\\s+(?<restaurantname>[^\\s]+)\\s+(?<foodname>[^\\s]+)"+"(\\s+\\-n\\s+(?<number>\\-?[0-9]+))?"+"\\s*";
        String showCartRegex="\\s*show\\s+cart\\s*";
        String showDiscountRegex="\\s*show\\s+discounts\\s*";
        String purchaseCartRegex="\\s*purchase\\s+cart"+"(\\s+\\-d\\s+(?<discountcode>[^\\s]+))?"+"\\s*";

        String input;
        String output="";

        while (keepGoing) {
            input=scanner.nextLine();


            Matcher logoutMatcher=Commands.giveTheMatcherStraightUp(input,logoutRegex);
            Matcher findMenuTypeMatcher=Commands.giveTheMatcherStraightUp(input,showMenuregex);
            Matcher chargeBalanceMatcher=Commands.giveTheMatcherStraightUp(input,chargeBalanceRegex);
            Matcher showBalanceMatcher=Commands.giveTheMatcherStraightUp(input, showBalanceregex);
            Matcher showRestaurantsMatcher=Commands.giveTheMatcherStraightUp(input, showRestaurantsRegex);
            Matcher showRestaurantMenuMatcher=Commands.giveTheMatcherStraightUp(input, showRestaurantMenuRegex);
            Matcher addToCartMatcher=Commands.giveTheMatcherStraightUp(input, addToCartRegex);
            Matcher removeFromCartMatcher=Commands.giveTheMatcherStraightUp(input,removeFromCartRegex);
            Matcher showCartMatcher=Commands.giveTheMatcherStraightUp(input, showCartRegex);
            Matcher showDiscountsMatcher=Commands.giveTheMatcherStraightUp(input, showDiscountRegex);
            Matcher purchaseCartMatcher=Commands.giveTheMatcherStraightUp(input, purchaseCartRegex);


            if(logoutMatcher.matches()){
                output="";
                keepGoing=false;
            }
            else if(findMenuTypeMatcher.matches()){
                output="customer menu"+"\n";
            }
            else if(chargeBalanceMatcher.matches()){
                int charge=Integer.parseInt(chargeBalanceMatcher.group("amount"));
                output=chargeBalance(currentCustomer, charge);
            }
            else if(showBalanceMatcher.matches()){
                output=showBalance(currentCustomer);
            }
            else if(showRestaurantsMatcher.matches()){
                String type=showRestaurantsMatcher.group("type");
                output=CostumerMenuController.showRestaurants(type);
            }
            else if(showRestaurantMenuMatcher.matches()){
                String name=showRestaurantMenuMatcher.group("name");
                String category=showRestaurantMenuMatcher.group("category");
                output=CostumerMenuController.showRestaurantMenu(name, category);
            }
            else if(addToCartMatcher.matches()){
                String restrName=addToCartMatcher.group("restaurantname");
                String foodName=addToCartMatcher.group("foodname");
                String count=addToCartMatcher.group("number");
                output=CostumerMenuController.addFoodToCart(restrName, foodName, count);
            }
            else if(removeFromCartMatcher.matches()){
                String restrName=removeFromCartMatcher.group("restaurantname");
                String foodName=removeFromCartMatcher.group("foodname");
                String count=removeFromCartMatcher.group("number");
                output=CostumerMenuController.removeFoodFromCart(restrName, foodName, count);
            }
            else if(showCartMatcher.matches()){
                output=CostumerMenuController.showCart();
            }
            else if(showDiscountsMatcher.matches()){
                output=CostumerMenuController.showDiscounts();
            }
            else if(purchaseCartMatcher.matches()){
                String tokenCode=purchaseCartMatcher.group("discountcode");
                output=CostumerMenuController.purchaseCart(tokenCode);
            }
            else output="invalid command!\n";



            System.out.print(output);
            output="";
        }
        keepGoing=true;
        setCurrentCustomer(null);
        MainMenu.setUser(null);
        LoginMenu.run(scanner);

    }

    private static void setCurrentCustomer(Customer customer){
        currentCustomer=customer;
    }

}
