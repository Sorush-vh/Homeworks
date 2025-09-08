package model;

public class Castle {
    private int hitpoint;
    private int damage;
    private User owningPlayer;
    private int castleSide;


    public Castle(User user,int side){

        if(side==1)
            this.hitpoint=3600*user.getLevel();
        else 
            this.hitpoint=2500*user.getLevel();
        
        this.damage=500*user.getLevel();
        this.castleSide=side;
        this.owningPlayer=user;

        user.getCastles().add(this);
    }

    public int getDamage(){
        return this.damage;
    }

    public int getHealth(){
        return this.hitpoint;
    }

    public int getSideFromLeft(){
        return castleSide;
    }

    public User getOwner(){
        return owningPlayer;
    }

    public void takeDamage(int damage){
        this.hitpoint-=damage;
    }
}
