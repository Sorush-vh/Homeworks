package model;
import java.util.ArrayList;

public class Restaurant extends NormalUsers {
    
    private ArrayList <Food> menu;
    private String type;


    public Restaurant(String name, String password, String type){
        super(name, password);
        this.type=type;
        Admin.addRestaurant(this);
        menu= new ArrayList<Food>();
    }

    public String getRestaurantType(){
        return this.type;
    }

    public ArrayList<Food> getMenu(){
        return menu;
    }

    public Food getFoodByName(String name){
        for (Food food : menu) {
            if(food.getName().equals(name)) return food;
        }
        return null;
    }

    public void addFood(Food food){
        menu.add(food);
    }
}
