package model;

public class BattleCard{
    private String cardName;
    private int cost;
    private User ownerInBattle;

    public BattleCard(String name, int cost){
        this.cardName=name;
        this.cost=cost;
    }
    public void setOwnerInBattle(User user){
        this.ownerInBattle=user;
    }

    public User getOwnerInBattle(){
        return ownerInBattle;
    }

    public String getCardName(){
        return cardName;
    }
    
    public int getCost(){
        return cost;
    }
}