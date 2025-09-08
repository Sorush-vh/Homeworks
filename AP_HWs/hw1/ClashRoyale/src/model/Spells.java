package model;

public class Spells extends BattleCard{
    private int healthEffect;
    private int usage;

    public Spells(String name, int cost, int healthEffect, int usage){
        super(name, cost);
        this.healthEffect=healthEffect;
        this.usage=usage;
    }


    public static Spells makeHealSpell(){
        Spells tempHeal=new Spells("Heal", 150, 1000, 2);
        return tempHeal;
    }

    public static Spells makeFireball(){
        Spells tempFireball= new Spells("Fireball", 100, 1600, 1);
        return tempFireball;
    }

    public int getRemainedDuration(){
        return usage;
    }

    public int getHealthEffect(){
        return this.healthEffect;
    }

    public void decreaseDuration(int i){
        usage-=i;
    }
}

