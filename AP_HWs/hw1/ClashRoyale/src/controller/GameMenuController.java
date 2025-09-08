package controller;


import model.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import view.Menu;
import view.GameMenu;


public class GameMenuController {
    
    public static int turnsCount;
    
    public static User host;
    public static User guest;
    public static User activeUser;
    public static User opponentUser;
    public static int playersCardDeployments;
    public static int playersTroopMovements;

    
    private static String showLineInfoRegex="show line info (?<lineDirection>.+)";
    private static String moveTroopsRegex="move troop in line (?<lineDirection>.+) and row (?<rowNumber>\\d+) (?<direction>.+)";
    private static String deployTroopsRegex="deploy troop (?<troopName>.+) in line (?<lineDirection>.+) and row (?<rowNumber>\\d+)";
    private static String deployHealRegex="deploy spell Heal in line (?<lineDirection>.+) and row (?<rowNumber>\\d+)";
    private static String deployFireballRegex="deploy spell Fireball in line (?<lineDirection>.+)";

    public static void setPlayersCastles(){
        for (int i = 0; i < 3; i++) {
            Castle tempHostCastle=new Castle(host, i);
            Castle tempGuestCastle=new Castle(guest, i);
        }
    }

    public static String handleOrder(String input){

        String output="";

        Matcher showLineInfoMatcher=Commands.giveTheMatcherStraightUp(input, showLineInfoRegex);
        Matcher moveTroopsMatcher=Commands.giveTheMatcherStraightUp(input, moveTroopsRegex);
        Matcher deployTroopsMatcher=Commands.giveTheMatcherStraightUp(input, deployTroopsRegex);
        Matcher deployHealMatcher=Commands.giveTheMatcherStraightUp(input, deployHealRegex);
        Matcher deployFireballMatcher=Commands.giveTheMatcherStraightUp(input, deployFireballRegex);


        if(input.equals(Menu.showCurrentMenuFormat)){
            output="Game Menu\n";
        }
        else if(input.equals("number of cards to play")){
            output=showRemainedDeployments();
        }
        else if(input.equals("number of moves left")){
            output=showRemainedMovements();
        }
        else if(input.equals("show the hitpoints left of my opponent")){
            output=showEnemysHitpoint();
        }
        else if(showLineInfoMatcher.matches()){
            String line=showLineInfoMatcher.group("lineDirection");
            output=showTroopsInLine(line);
        }
        else if(moveTroopsMatcher.matches()){
            String lineDirection=moveTroopsMatcher.group("lineDirection");
            String row=moveTroopsMatcher.group("rowNumber");
            String direction=moveTroopsMatcher.group("direction");
            output=moveTroop(row, lineDirection, direction);
        }
        else if(deployTroopsMatcher.matches()){

            String lineDirection=deployTroopsMatcher.group("lineDirection");
            String row=deployTroopsMatcher.group("rowNumber");
            String troopName=deployTroopsMatcher.group("troopName");
            output=deployTroop(troopName, lineDirection, row);
        }
        else if(deployHealMatcher.matches()){
            String lineDirection=deployHealMatcher.group("lineDirection");
            String row=deployHealMatcher.group("rowNumber");
            output=deployHeal(lineDirection, row);
        }
        else if(deployFireballMatcher.matches()){
            String lineDirection=deployFireballMatcher.group("lineDirection");
            output=deployFireball(lineDirection);
        }
        else if(input.equals("next turn"))
        output="";

        else output="Invalid command!\n";

        return output;
    }

    private static String showEnemysHitpoint(){
        int midHp,leftHp,rightHp;
        String output="";

        midHp=opponentUser.getCastleBySide("middle").getHealth();
        leftHp=opponentUser.getCastleBySide("left").getHealth();
        rightHp=opponentUser.getCastleBySide("right").getHealth();

        if(midHp<=0) midHp=-1;
        if(leftHp<=0) leftHp=-1;
        if(rightHp<=0) rightHp=-1;

        output=output.concat("middle castle: "+midHp+"\n");
        output=output.concat("left castle: "+leftHp+"\n");
        output=output.concat("right castle: "+rightHp+"\n");

        return output;
    }

    private static String showTroopsInLine(String line){
        if(!isLineValid(line)) return "Incorrect line direction!\n";

        int lineIndex=getLineInNumber(line);
        String output=line+" line:\n";

        for (int i = 0; i < 15; i++) {

            if(GameMenu.mapIndexes[i][lineIndex].size()==0) continue;

            
            for (BattleCard card : GameMenu.mapIndexes[i][lineIndex]) {
                output=output.concat("row "+(i+1)+": ");
                output=output.concat(card.getCardName()+": ");
                output=output.concat(card.getOwnerInBattle().getUsername());
                output=output.concat("\n");
            }
            
        }
        
        return output;
    }

    private static String showRemainedDeployments(){
        return "You can play "+playersCardDeployments+" cards more!\n";
    }

    private static String showRemainedMovements(){
        return "You have "+playersTroopMovements+" moves left!\n";
    }

    private static String moveTroop(String row, String lineDirection, String direction){
        if(!isLineValid(lineDirection)) return "Incorrect line direction!\n";

        int rowInNumber=Integer.parseInt(row);
        if(rowInNumber<1||rowInNumber>15) return "Invalid row number!\n";

        int directionInNumber=parseDirection(direction);
        if(directionInNumber==-1) return "you can only move troops upward or downward!\n";

        if(playersTroopMovements==0) return "You are out of moves!\n";

        int lineInNumber=getLineInNumber(lineDirection);
        Troops targetCard=null;
        
        rowInNumber--;
        for (BattleCard card : GameMenu.mapIndexes[rowInNumber][lineInNumber]) {
            if(!isCardForPlayer(activeUser, card) ||
                !(card instanceof Troops) ) continue;

            targetCard=(Troops) card;
            break;
        }

        if(targetCard==null) return "You don't have any troops in this place!\n";

        rowInNumber++;
        if(!isMoveValid(rowInNumber, directionInNumber)) return "Invalid move!\n";


        rowInNumber--;
        GameMenu.mapIndexes[rowInNumber][lineInNumber].remove(targetCard);
        int finalRow=getFinalRow(rowInNumber, directionInNumber);

        GameMenu.mapIndexes[finalRow][lineInNumber].add(targetCard);
        playersTroopMovements--;
        return targetCard.getCardName()+" moved successfully to row "+(finalRow+1)+" in line "+lineDirection+"\n";
    }

    private static String deployTroop(String troopName, String lineDirection, String row){
        
        int troopType=getTroopType(troopName);
        if(troopType==-1) return "Invalid troop name!\n";

        if(activeUser.getCardFromDeckByName(troopName)==null)
          return "You don't have "+troopName+" card in your battle deck!\n";

        if(!isLineValid(lineDirection)) return "Incorrect line direction!\n";
        
        int rowInNumber=Integer.parseInt(row);
        if(rowInNumber<1||rowInNumber>15) return "Invalid row number!\n";

        if(!isTargetRowValid(rowInNumber)) return "Deploy your troops near your castles!\n";

        if(playersCardDeployments==0) return "You have deployed a troop or spell this turn!\n";

        rowInNumber--;
        int lineInNumber=getLineInNumber(lineDirection);
        Troops targetTroop=generateTroopByType(troopType);
        targetTroop.setOwnerInBattle(activeUser);
        GameMenu.mapIndexes[rowInNumber][lineInNumber].add(targetTroop);
        playersCardDeployments--;

        return "You have deployed "+troopName+" successfully!\n";
    }

    private static String deployFireball(String lineDirection){
        if(!isLineValid(lineDirection)) return "Incorrect line direction!\n";


        if(activeUser.getCardFromDeckByName("Fireball")==null)
         return "You don't have Fireball card in your battle deck!\n";

        if(playersCardDeployments==0) return "You have deployed a troop or spell this turn!\n";

        Castle targetCastle=opponentUser.getCastleBySide(lineDirection);
        if(targetCastle.getHealth()<=0) return "This castle is already destroyed!\n";

        Spells fireball=Spells.makeFireball();
        targetCastle.takeDamage(fireball.getHealthEffect());
        playersCardDeployments--;

        return "You have deployed Fireball successfully!\n";
    }

    private static String deployHeal(String lineDirection, String row){
        if(!isLineValid(lineDirection)) return "Incorrect line direction!\n";

        int lineInNumber=getLineInNumber(lineDirection);
        int rowInNumber =Integer.parseInt(row);

        if(activeUser.getCardFromDeckByName("Heal")==null) 
          return "You don't have Heal card in your battle deck!\n";

        if(rowInNumber<1||rowInNumber>15) return "Invalid row number!\n";

        if(playersCardDeployments==0) return "You have deployed a troop or spell this turn!\n";

        Spells targetHeal=Spells.makeHealSpell();
        targetHeal.setOwnerInBattle(activeUser);
        rowInNumber--;
        GameMenu.mapIndexes[rowInNumber][lineInNumber].add(targetHeal);
        playersCardDeployments--;
        return "You have deployed Heal successfully!\n";
    }

    private static Troops generateTroopByType(int type){
        Troops troop;
        if(type==1) troop=Troops.makeBarbarian();
        else if(type==2) troop=Troops.makeIceWizard();
        else troop=Troops.makeBabyDragon();

        troop.setOwnerInBattle(activeUser);
        return troop;
    }

    private static boolean isLineValid(String line){
        String lineRegex="(left)|(right)|(middle)";
        Matcher lineMatcher=Commands.giveTheMatcherStraightUp(line, lineRegex);
        if(lineMatcher.matches()) return true;
        return false;
    }

    private static int parseDirection(String direction){
        if(direction.equals("upward")) return 1;
        if(direction.equals("downward")) return 0;
        return -1;
    }

    private static int getLineInNumber(String line){
        if(line.equals("left")) return 0;
        else if(line.equals("right")) return 2;
        return 1;
    }

    private static boolean isCardForPlayer(User player, BattleCard targetCard){
        if(targetCard.getOwnerInBattle().getUsername().equals(player.getUsername()))
         return true;
        return false; 
    }

    private static boolean isMoveValid(int row, int direction){
        if(row==15 && direction ==1) return false;
        if(row==1 && direction==0) return false;
        return true;
    }

    private static int getTroopType(String troopName){
        if(troopName.equals("Barbarian")) return 1;
        if(troopName.equals("Ice Wizard")) return 2;
        if(troopName.equals("Baby Dragon")) return 3;
        return -1;
    }

    private static int getFinalRow(int row, int direction){
        if(direction==1) return row+1;
        return row-1;
    }

    private static boolean isTargetRowValid(int row){
        if(activeUser.getUsername().equals(host.getUsername())){
            if(row>4) return false;
        }
        else{
            if(row<12) return false;
        }
        return true;
    }

    private static void handleTroopFights(){
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 3; j++) {

                for (BattleCard targetTroop : GameMenu.mapIndexes[i][j]) {

                    if(!(targetTroop instanceof Troops))
                    continue;

                    for (BattleCard tempTroop : GameMenu.mapIndexes[i][j]) {
                        
                        if(!(tempTroop instanceof Troops) || isCardForPlayer(targetTroop.getOwnerInBattle(), tempTroop))
                        continue;

                        Troops troop1=(Troops)targetTroop  ,  troop2=(Troops)tempTroop;
                        int dealtDamage=troop1.getDamage()-troop2.getDamage();

                        if(dealtDamage>0) troop2.decreaseHealth(dealtDamage);
                    }
                }

            }
        }
    }

    private static void handleCastleFights(){
        for (int i = 0; i < 3; i++) {

            Castle hostTargetCastle=host.getCastleByIndex(i);
            for (BattleCard card : GameMenu.mapIndexes[0][i]) {

                if(hostTargetCastle.getHealth()<=0) break;

                if(isCardForPlayer(guest, card)&&(card instanceof Troops)){

                    Troops enemyTroop=(Troops) card;
                    enemyTroop.decreaseHealth(hostTargetCastle.getDamage()*host.getLevel());
                    hostTargetCastle.takeDamage(enemyTroop.getDamage());

                }
            }

            Castle guestTargetcastle=guest.getCastleByIndex(i);
            for (BattleCard card : GameMenu.mapIndexes[14][i]) {
                
                if(guestTargetcastle.getHealth()<=0) break;

                if(isCardForPlayer(host, card)&& (card instanceof Troops)){

                    Troops enemyTroop=(Troops) card;
                    enemyTroop.decreaseHealth(guestTargetcastle.getDamage()*guest.getLevel());
                    guestTargetcastle.takeDamage(enemyTroop.getDamage());
                }
            }


        }
    }

    private static void handleHealSpells(){
        for (int i = 0; i < 15; i++) {
            
            for (int j = 0; j < 3; j++) {
                
                for (BattleCard card : GameMenu.mapIndexes[i][j]) {
                    if(card instanceof Spells){
                        Spells targetHeal=(Spells) card;

                        for (BattleCard troops : GameMenu.mapIndexes[i][j]) {
                            
                            if(troops instanceof Spells) continue;

                            Troops targetTroop=(Troops) troops;
                            if(isCardForPlayer(targetHeal.getOwnerInBattle(), targetTroop)){
                                targetTroop.decreaseHealth(-targetHeal.getHealthEffect());
                                if(targetTroop.getHealth()>targetTroop.getBaseHealth())
                                    targetTroop.setHealth(targetTroop.getBaseHealth());
                            }
                        }

                    }
                }

            }

        }
    }

    private static void handleFinishedCards(){
        ArrayList<BattleCard> shouldBeRemoved=new ArrayList<BattleCard>();
        for (int i = 0; i < 15; i++) {
            
            for (int j = 0; j < 3; j++) {
                shouldBeRemoved.clear();

                for (BattleCard card : GameMenu.mapIndexes[i][j]) {
                    if(card instanceof Spells){
                        Spells tempHeal=(Spells) card;
                        tempHeal.decreaseDuration(1);

                        if(tempHeal.getRemainedDuration()==0)
                        shouldBeRemoved.add(tempHeal);
                    }
                    else{
                        Troops tempTroop=(Troops) card;
                        
                        if(tempTroop.getHealth()<=0) 
                        shouldBeRemoved.add(tempTroop);
                    }
                }

                for (BattleCard battleCard : shouldBeRemoved) {
                    GameMenu.mapIndexes[i][j].remove(battleCard);
                }

            }

        }
    }

    private static void changeTurnOfUsers(){
        if(activeUser.getUsername().equals(host.getUsername())) {
            activeUser=guest;
            opponentUser=host;
        }
        else {
            activeUser=host;
            opponentUser=guest;
        }

        playersCardDeployments=1;
        playersTroopMovements=3;
    }

    public static void handleEndOfTurn(){
        if(activeUser.getUsername().equals(guest.getUsername())){
            handleTroopFights();
            handleCastleFights();
            handleHealSpells();
            handleFinishedCards();
            turnsCount--;
        }

        changeTurnOfUsers();
    }

    public static boolean isGameFinished(){

        if(guest.getCastleByIndex(0).getHealth() <=0 &&
           guest.getCastleByIndex(1).getHealth() <=0 &&
           guest.getCastleByIndex(2).getHealth() <=0)
           return true;


         if(host.getCastleByIndex(0).getHealth() <=0 &&
            host.getCastleByIndex(1).getHealth() <=0 &&
            host.getCastleByIndex(2).getHealth() <=0)
            return true;


            return false;
    }

    private static void giveGoldToPlayers(){
        Castle hostCastle,guestcastle;
        for (int i = 0; i < 3; i++) {
            hostCastle=host.getCastleByIndex(i);
            guestcastle=guest.getCastleByIndex(i);

            if(hostCastle.getHealth()<=0) guest.changeGoldAmount(25);
            if(guestcastle.getHealth()<=0) host.changeGoldAmount(25);
        }
    }

    public static void handlePointsAndWinner(){
        int guestXpSum=guest.getCastlesEquivalentXp();
        int hostXpSum=host.getCastlesEquivalentXp();

        host.addXpAccordingly(hostXpSum);
        guest.addXpAccordingly(guestXpSum);

        giveGoldToPlayers();

        User winner=null;
        if(guestXpSum>hostXpSum) winner=guest;
        if(guestXpSum<hostXpSum) winner=host;

        if(winner !=null) {
            System.out.println("Game has ended. Winner: "+winner.getUsername());
        }
        else System.out.println("Game has ended. Result: Tie");
    }
}
