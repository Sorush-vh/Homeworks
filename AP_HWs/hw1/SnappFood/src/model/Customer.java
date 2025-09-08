package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Customer extends NormalUsers {
    private ArrayList<Food> cart;
    private ArrayList<Discounts> discounts;
    private HashMap<Food, Integer> demandedFoodNumber;

    public Customer(String username, String password){
        super(username, password);
        Admin.addCustomer(this);
        cart= new ArrayList<Food>();
        discounts= new ArrayList<Discounts>();
        demandedFoodNumber= new HashMap<Food ,Integer>();
    }

    public ArrayList<Food> getCart(){
        return cart;
    }

    public void addFoodToCustomersCart(Food targetFood, int number){
        if(getDuplicateFoodFromCart(targetFood)==null){
        getCart().add(targetFood);
        demandedFoodNumber.put(targetFood,number);
        }
        else{
            int oldNumber=demandedFoodNumber.get(targetFood);
            demandedFoodNumber.remove(targetFood);
            demandedFoodNumber.put(targetFood,oldNumber+number);
        }

    }

    public HashMap<Food, Integer> getDemandedNumbers(){
        return demandedFoodNumber;
    }

    public void addDiscount(Discounts token){
        getDiscounts().add(token);
    }

    public int getTotalFoodPrice(Food ownedFood){
       int wantedNumber= demandedFoodNumber.get(ownedFood);
       return wantedNumber*ownedFood.getPrice();
    }

    public int getTotalCartPrice(){
        int totalMoney=0;

        for (Food food : cart) {
            totalMoney+=getTotalFoodPrice(food);
        }
        
        return totalMoney;
    }

    public ArrayList<Discounts> getDiscounts(){
        return discounts;
    }

    public Discounts getDiscountByCode(String code){
        for (Discounts token : getDiscounts()) {
            if(token.getCode().equals(code)) return token;
        }
        return null;
    }

    private Food getDuplicateFoodFromCart(Food targetFood ){
        for (Food food : cart) {
            if(food.getName().equals(targetFood.getName())
                && food.getOwningRestaurant().getUsername().equals(targetFood.getOwningRestaurant().getUsername()))
                return food;
        }
        return null;
    }
}
