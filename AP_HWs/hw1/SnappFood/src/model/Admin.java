package model;
import java.util.ArrayList;

public class Admin extends User {
    
    private static ArrayList<User> allActiveAccounts;
    private static ArrayList<Restaurant> activeRestaurants;
    private static ArrayList<Customer> activeCustomers;
    private static ArrayList<Discounts> allDiscounts;
    private static Admin admin=null;

    static{
        allActiveAccounts=new ArrayList<User>();
        activeCustomers= new ArrayList<Customer>();
        activeRestaurants= new ArrayList<Restaurant>();
        allDiscounts=new ArrayList<Discounts>();
    }

    public Admin( String username, String password){
        super(username, password);
        admin=this;
    }

    public static Admin getAdmin(){
        return admin;
    }

    public static ArrayList<User> getActiveUsers(){
        return allActiveAccounts;
    }

    public static ArrayList<Restaurant> getActiveRestaurants(){
        return activeRestaurants;
    }

    public static ArrayList<Discounts> getAllDiscounts(){
        return allDiscounts;
    }

    public static ArrayList<Customer> getActiveCustomers(){
        return activeCustomers;
    }

    public static void removeUserByUsername(String username){
        //note: only valid names are handled, remove function wasnt used before
        User targetUser=getUserByUsername(username);

        getActiveUsers().remove(targetUser);

        if(targetUser instanceof Customer) 
            terminateCustomer((Customer) targetUser);
        else 
            terminateRestaurant((Restaurant) targetUser);
    }

    public static void addUser(User user){
        getActiveUsers().add(user);
    }

    public static void addCustomer(Customer costumer){
        activeCustomers.add(costumer);
    }

    public static void addRestaurant(Restaurant restaurant){
        activeRestaurants.add(restaurant);
    }

    public static void addDiscount(Discounts token){
        getAllDiscounts().add(token);
    }

    public static User getUserByUsername(String username){

        for (User user : getActiveUsers()) {
            if(user.getUsername().equals(username))
            return user;
        }
        return null;
    }

    public static Restaurant getRestaurantByName(String name){
        for (Restaurant restaurant : getActiveRestaurants()) {
            if(restaurant.getUsername().equals(name)) return restaurant;
        }
        return null;
    }

    public static Customer getCustomerByUsername(String username){
        for (Customer costumer : getActiveCustomers()) {
            if(costumer.getUsername().equals(username)) return costumer;
        }
        return null;
    }

    private static void terminateCustomer(Customer targetCustomer){
        getActiveCustomers().remove(targetCustomer);
    }

    private static void terminateRestaurant(Restaurant targetRestaurant){
        getActiveRestaurants().remove(targetRestaurant);

        for (Customer customer : getActiveCustomers()) {

            for (Food food : customer.getCart()) {

                if(food.getOwningRestaurant().getUsername().equals(targetRestaurant.getUsername()))
                    customer.getCart().remove(food);

            }
        }
    }
}
