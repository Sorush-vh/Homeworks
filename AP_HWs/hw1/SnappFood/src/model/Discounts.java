package model;
public class Discounts {
    private String code;
    private int value;
    private Customer owningCustomer;

    public Discounts(String code, int value, Customer owningCustomer){
        this.value=value;
        this.owningCustomer=owningCustomer;
        this.code=code;

        owningCustomer.addDiscount(this);
        Admin.addDiscount(this);
    }

    public String getCode(){
        return code;
    }

    public int getValue(){
        return value;
    }

    public Customer getOwner(){
        return owningCustomer;
    }
}
