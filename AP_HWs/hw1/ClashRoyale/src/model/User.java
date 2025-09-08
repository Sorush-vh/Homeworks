package model;
import controller.Server;


import java.util.ArrayList;

public class User {
    private int experience;
    private int level;
    private int goldAmount;
    private String username;
    private String password;
    private ArrayList<BattleCard> deck;
    private ArrayList<BattleCard> acquiredCards;
    private ArrayList<Castle> battleCastles;


    public User( String username, String password){
        this.experience=0;
        this.level=1;
        this.goldAmount=100;
        this.username=username;
        this.password=password;

        this.deck=new ArrayList<BattleCard>();
        this.acquiredCards= new ArrayList<BattleCard>();
        this.battleCastles= new ArrayList<Castle>();
        

        Server.addUser(this);
    }

    public void RemoveAcquiredCardByName(String name){
        BattleCard targetCard=null;
        for (BattleCard battleCard : acquiredCards) {
            if(battleCard.getCardName().equals(name))
            targetCard=battleCard;
        }
        acquiredCards.remove(targetCard);
    }

    public ArrayList<Castle> getCastles(){
        return battleCastles;
    }

    public int getCastlesEquivalentXp(){
        int sum=0;
        for (int j = 0; j < 3; j++) {
            Castle castle=getCastleByIndex(j);
            if(castle.getHealth()>0) sum+=castle.getHealth();
        }
        return sum;
    }

    public Castle getCastleBySide(String side){
        if(side.equals("left")) return battleCastles.get(0);
        if(side.equals("middle")) return battleCastles.get(1);
        if(side.equals("right")) return battleCastles.get(2);
        return null;
    }

    public Castle getCastleByIndex(int i){
        if(i==0) return getCastleBySide("left");
        if(i==1) return getCastleBySide("middle");
        if(i==2) return getCastleBySide("right");
        return null;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public int getLevel(){
        return level;
    }

    public int getExperience(){
        return experience;
    }

    public int getGoldAmount(){
        return goldAmount;
    }

    public void changeGoldAmount(int mode){
        goldAmount+=mode;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public BattleCard getUsersOwnedCardByName(String name){
        for (BattleCard battleCard : acquiredCards) {
            if(battleCard.getCardName().equals(name)) return battleCard;
        }
        return null;
    }

    public BattleCard getCardFromDeckByName(String name){
        for (BattleCard battleCard : deck) {
            if(battleCard.getCardName().equals(name)) return battleCard;
        }
        return null;
    }

    public ArrayList<BattleCard> getDeck(){
        return deck;
    }

    public ArrayList<BattleCard> getAcquiredCards(){
        return acquiredCards;
    }

    public BattleCard getDeckCardBylexigographicOrder(int rank){

        int targetIndex;
        BattleCard targetCard,tempCard;

        for (int i = 0; i < deck.size(); i++) {
            targetIndex=1;
            targetCard=deck.get(i);

            for (int j = 0; j < deck.size(); j++) {
                if(j==i) continue;
                tempCard=deck.get(j);
                
                if(targetCard.getCardName().compareTo(tempCard.getCardName()) >0){
                     targetIndex++;
                }
            }

            if(targetIndex==rank) return targetCard;
        }

        return null;
    }

    public void addXpAccordingly(int xp){
        experience+=xp;
        levelUpIfNeeded();
    }

    private void levelUpIfNeeded(){
        int levelUpNeededXp=160*level*level;

        if(experience>=levelUpNeededXp){
            experience-=levelUpNeededXp;
            level++;
            levelUpIfNeeded();
        }
    }

}
