package controller;

import model.*;
import controller.Server;
import view.ShopMenu;
import view.Menu;

public class ShopMenuController {
    

    public static String buyCard( String cardName){

        if(!Menu.isCardNameValid(cardName)) return "Invalid card name!\n";

        BattleCard targetCard=getCardByName(cardName);

        if(targetCard.getCost()>Server.getCurrentUser().getGoldAmount())
         return "Not enough gold to buy "+ cardName+ "!\n";

        if(Server.getCurrentUser().getUsersOwnedCardByName(cardName)!=null) return "You have this card!\n";

        Server.getCurrentUser().getAcquiredCards().add(targetCard);
        Server.getCurrentUser().changeGoldAmount(-targetCard.getCost());

        return "Card "+cardName+" bought successfully!\n";
    }

    public static String sellCard(String cardName){

 

        if(!Menu.isCardNameValid(cardName)) return "Invalid card name!\n";

        BattleCard targetCard=getCardByName(cardName);

        if(Server.getCurrentUser().getUsersOwnedCardByName(cardName)==null)
             return "You don't have this card!\n";

        if(Server.getCurrentUser().getCardFromDeckByName(cardName)!=null)
             return "You cannot sell a card from your battle deck!\n";


        int addedGold=(8 * targetCard.getCost())/10 ;

        Server.getCurrentUser().changeGoldAmount(addedGold);
        Server.getCurrentUser().RemoveAcquiredCardByName(cardName);

        return "Card "+cardName+" sold successfully!\n";
    }

    private static BattleCard getCardByName(String name){
        for (BattleCard battleCard : ShopMenu.shop) {
            if(battleCard.getCardName().equals(name)) return battleCard;
        }
        return null;
    }

}
