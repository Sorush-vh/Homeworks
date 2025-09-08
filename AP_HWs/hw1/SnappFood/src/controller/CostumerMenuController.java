package controller;
import model.*;
import view.NormalMenu;

public class CostumerMenuController {
    
    private static Customer currentCustomer;

    public static String showRestaurants(String type){

        int index=1;
        String output="";

        if(type==null){

            for (Restaurant restaurant : Admin.getActiveRestaurants()) {
                output=output.concat(fillCustomerOutputForValidRestaurant(restaurant, index));
                index++;
            }
        }

        else {

            for (Restaurant restaurant : Admin.getActiveRestaurants()) {
                
                if(restaurant.getRestaurantType().equals(type)) {
                    output=output.concat(fillCustomerOutputForValidRestaurant(restaurant, index));
                    index++;
                }
            }
        }
        return output;
    }

    public static String showRestaurantMenu(String restaurantName, String category){
        Restaurant targetRestaurant=Admin.getRestaurantByName(restaurantName);
        String output="";

        if(targetRestaurant==null) return "show menu failed: restaurant not found\n";

        if(category != null)
            if(!NormalMenu.isFoodCategoryValid(category)) return "show menu failed: invalid category\n";



        if(category != null){
            for (Food food : targetRestaurant.getMenu()) {
                if(food.getCategory().equals(category))
                    output=output.concat(fillMenuOutputForFood(food));
            }
        }

        else{
            output=output.concat("<< STARTER >>\n");
            for (Food food : targetRestaurant.getMenu()) {
                if(food.getCategory().equals("starter"))
                    output=output.concat(fillMenuOutputForFood(food));
            }  
            
            output=output.concat("<< ENTREE >>\n");
            for (Food food : targetRestaurant.getMenu()) {
                if(food.getCategory().equals("entree"))
                    output=output.concat(fillMenuOutputForFood(food));
            }

            output=output.concat("<< DESSERT >>\n");
            for (Food food : targetRestaurant.getMenu()) {
                if(food.getCategory().equals("dessert"))
                    output=output.concat(fillMenuOutputForFood(food));
            }
        }
        return output;
    }

    public static String fillCustomerOutputForValidRestaurant(Restaurant restaurant, int index){
        String output="";
        output=output.concat(""+index+") ");
        output=output.concat(restaurant.getUsername()+": ");
        output=output.concat("type="+restaurant.getRestaurantType());
        output=output.concat("\n");
        return output;
    }

    public static String fillMenuOutputForFood(Food food){
        String output="";

        output=output.concat(food.getName()+" | ");
        output=output.concat("price="+ food.getPrice());
        output=output.concat("\n");

        return output;
    }

    public static String addFoodToCart(String targetRestaurantName, String FoodName, String number){
        int demandedCount;
        if(number!= null) demandedCount=Integer.parseInt(number);
        else demandedCount=1;

        Restaurant targetRestaurant=Admin.getRestaurantByName(targetRestaurantName);
        if(targetRestaurant ==null) return "add to cart failed: restaurant not found\n";

        Food food=targetRestaurant.getFoodByName(FoodName);
        if(food==null) return "add to cart failed: food not found\n";

        if(demandedCount<=0) return "add to cart failed: invalid number\n";

        currentCustomer.addFoodToCustomersCart(food, demandedCount);
        return "add to cart successful\n";
    }

    public static String removeFoodFromCart(String targetRestaurantName, String FoodName, String number){
        int demandedCount;
        if(number!= null) demandedCount=Integer.parseInt(number);
        else demandedCount=1;
        Restaurant targetRestaurant=Admin.getRestaurantByName(targetRestaurantName);

        Food targetFood=null;
        for (Food food : currentCustomer.getCart()) {

            if(food.getName().equals(FoodName) &&
               food.getOwningRestaurant().getUsername().equals(targetRestaurant.getUsername() )){
                    targetFood=food;
                    break;
            }
        }

        if(targetFood == null) return "remove from cart failed: not in cart\n";

        if(demandedCount <=0) return "remove from cart failed: invalid number\n";

        int oldDemand=currentCustomer.getDemandedNumbers().get(targetFood);
        if(oldDemand<demandedCount) return "remove from cart failed: not enough food in cart\n";

        currentCustomer.getDemandedNumbers().remove(targetFood);
        currentCustomer.getDemandedNumbers().put(targetFood, oldDemand-demandedCount);
        return "remove from cart successful\n";

    }

    public static String showDiscounts(){
        String output="";
        int index=1;

        //[index]) [code] | amount=[amount]
        for (Discounts token : currentCustomer.getDiscounts()) {
            output=output.concat(""+index+") ");
            output=output.concat(token.getCode()+" | ");
            output=output.concat("amount="+token.getValue());
            output=output.concat("\n");

            index++;
        }
        return output;
    }

    public static String showCart(){
        String output="";
        int index=1;
        int totalExpenses=0;
        //[index]) [food name] | restaurant=[restaurant name] price=[price]
        for (Food food : currentCustomer.getCart() ) {
            output=output.concat(""+index+") ");
            output=output.concat(food.getName()+" | ");
            output=output.concat("restaurant="+food.getOwningRestaurant().getUsername()+" ");
            output=output.concat("price="+currentCustomer.getTotalFoodPrice(food));
            output=output.concat("\n");

            totalExpenses+=currentCustomer.getTotalFoodPrice(food);
            index++;
        }
        output=output.concat("Total: "+totalExpenses+"\n");

        return output;
    }

    public static String purchaseCart(String discountCode){
        Discounts token=null;
        
        if(discountCode != null){
            token=currentCustomer.getDiscountByCode(discountCode);

            if(token==null) return "purchase failed: invalid discount code\n";
        }


        int discountBonus=0;
            if(token != null) discountBonus=token.getValue();

        int totalCost=currentCustomer.getTotalCartPrice()-discountBonus;
        if(currentCustomer.getBalance()<totalCost) return "purchase failed: inadequate money\n";



        Restaurant targetRestaurant;
        int restaurantsBenefit;
        int orderedNumber;

        for (Food food : currentCustomer.getCart()) {

            targetRestaurant=food.getOwningRestaurant();
            
            orderedNumber=currentCustomer.getDemandedNumbers().get(food);
            restaurantsBenefit=orderedNumber*(food.getPrice()-food.getCost());
            
            targetRestaurant.addBalance(restaurantsBenefit);

        }

        currentCustomer.getCart().clear();
        currentCustomer.getDemandedNumbers().clear();

        currentCustomer.addBalance(-totalCost);
        if(token != null){
            currentCustomer.getDiscounts().remove(token);
            Admin.getAllDiscounts().remove(token);
        }

        return "purchase successful\n";
    }

    public static void setCurrentCustomer(Customer customer){
        CostumerMenuController.currentCustomer=customer;
    }

}
