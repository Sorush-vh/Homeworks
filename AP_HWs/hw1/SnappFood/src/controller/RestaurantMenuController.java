package controller;
import model.*;
import view.NormalMenu;

public class RestaurantMenuController {
    
    private static Restaurant currentRestaurant;


    public static String addFoodToMenu(String name, String category, int price, int cost){
        
        if(!NormalMenu.isFoodCategoryValid(category)) return "add food failed: invalid category\n";

        if(!NormalMenu.isTypeOrFoodValid(name)) return "add food failed: invalid food name\n";

        if(currentRestaurant.getFoodByName(name) != null) return "add food failed: food already exists\n";

        if( (price <=0 ) || (cost <=0 )) return "add food failed: invalid cost or price\n";

        Food tempFood=new Food(name, category, price, cost, currentRestaurant);

        return "add food successful\n";
    }

    public static String removeFoodFromMenu(String foodName){
        Food targetFood=currentRestaurant.getFoodByName(foodName);

        if(targetFood==null) return "remove food failed: food not found\n";

        currentRestaurant.getMenu().remove(targetFood);
        return "";
    }

    public static void setCurrentRestaurant(Restaurant restaurant){
        currentRestaurant=restaurant;
    }


}
