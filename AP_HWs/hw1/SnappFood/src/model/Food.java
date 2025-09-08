package model;
public class Food {
    private String name;
    private String category;
    private int price;
    private int cost;
    private Restaurant owningRestaurant;

    public Food(String name, String category , int price , int cost , Restaurant owningRestaurant){
        this.name=name;
        this.category=category;
        this.price=price;
        this.cost=cost;
        this.owningRestaurant=owningRestaurant;

        owningRestaurant.addFood(this);
    }

    public String getName(){
        return this.name;
    }

    public String getCategory(){
        return this.category;
    }

    public int getPrice(){
        return this.price;
    }

    public int getCost(){
        return this.cost;
    }

    public Restaurant getOwningRestaurant(){
        return this.owningRestaurant;
    }
}
