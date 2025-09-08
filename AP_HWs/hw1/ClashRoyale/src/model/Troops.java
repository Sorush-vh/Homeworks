package model;

public class Troops extends BattleCard {
    private int health;
    private int damage;
    private int BaseHealth;

    public Troops(String name, int cost, int health , int damage){
        super(name,cost);
        this.damage=damage;
        this.health=health;
        this.BaseHealth=health;
    }

    public int getHealth(){
        return this.health;
    }

    public int getBaseHealth(){
        return BaseHealth;
    }

    public void setHealth(int hp){
        this.health=hp;
    }

    public int getDamage(){
        return this.damage;
    }

    public void decreaseHealth(int mode){
        this.health-=mode;
    }

    public static Troops makeBarbarian(){
        Troops tempBarbarian= new Troops("Barbarian", 100, 2000, 900);
        return tempBarbarian;
    }

    public static Troops makeIceWizard(){
        Troops tempIceWizard= new Troops("Ice Wizard", 180, 3500, 1500);
        return tempIceWizard;
    }

    public static Troops makeBabyDragon(){
        Troops tempBabyDragon= new Troops("Baby Dragon", 200, 3300, 1200);
        return tempBabyDragon;
    }
}
