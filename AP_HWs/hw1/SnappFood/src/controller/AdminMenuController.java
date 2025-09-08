package controller;
import view.Menu;
import model.*;

public class AdminMenuController {
    public static String addRestaurant(String name, String password, String type){

        if(!Menu.isUsernameOrPasswordFormatValid(name)) return "add restaurant failed: invalid username format\n";

        if(Admin.getUserByUsername(name) != null) return "add restaurant failed: username already exists\n";

        if(!Menu.isUsernameOrPasswordFormatValid(password)) return "add restaurant failed: invalid password format\n";
    
        if(!Menu.isPasswordStrong(password)) return "add restaurant failed: weak password\n";

        if(!Menu.isTypeOrFoodValid(type)) return "add restaurant failed: invalid type format\n";

        Restaurant temp= new Restaurant(name, password, type);
        return "add restaurant successful\n";
    }

    public static String showRestaurants(String type){
        int index=1;
        String output="";
        if( type==null ){
            for (Restaurant restaurant : Admin.getActiveRestaurants()) {
                output=output.concat(fillOutputForValidRestaurant(restaurant, index));
                index++;
            }
        }

        else{
            for (Restaurant restaurant : Admin.getActiveRestaurants()) {
                if(restaurant.getRestaurantType().equals(type)){
                    output=output.concat(fillOutputForValidRestaurant(restaurant, index));
                    index++;
                }
            }
        }

        return output;
    }

    public static String fillOutputForValidRestaurant(Restaurant restaurant, int index){
        String output="";
        output=output.concat(""+index+") ");
        output=output.concat(restaurant.getUsername()+": ");
        output=output.concat("type="+restaurant.getRestaurantType()+" ");
        output=output.concat("balance="+restaurant.getBalance());
        output=output.concat("\n");
        return output;
    }

    public static String removeRestaurant(String name){
        Restaurant targetRestaurant=Admin.getRestaurantByName(name);
        
        if(targetRestaurant==null) return "remove restaurant failed: restaurant not found\n";

        Admin.removeUserByUsername(name);

        return "";
    }

    public static String setDiscount(String username, int amount, String code){
        Customer targetUser=Admin.getCustomerByUsername(username);
        
        if(targetUser==null) return "set discount failed: username not found\n";

        if(amount<=0) return "set discount failed: invalid amount\n";

        if(!Menu.isDiscountCodeValid(code)) return "set discount failed: invalid code format\n";

        Discounts tempToken=new Discounts(code, amount, targetUser);

        return "set discount successful\n";
    }

    public static String showDiscounts(){
        String output="";
        int index=1;
        for (Discounts token : Admin.getAllDiscounts()) {
            output=output.concat(fillOutputForValidDiscount(token, index)); 
            index++;
        }
        return output;
    }

    public static String fillOutputForValidDiscount(Discounts token, int index){
        //[index]) [code] | amount=[amount] --> user=[username]
        String output="";
        output=output.concat(""+index+") ");
        output=output.concat(token.getCode()+" ");
        output=output.concat("| amount="+token.getValue()+" ");
        output=output.concat("--> user="+token.getOwner().getUsername());
        output=output.concat("\n");
        return output;
    }

}
