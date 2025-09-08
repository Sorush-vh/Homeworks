package controller;

import model.*;
import view.Menu;

public class ProfileMenuController {
    
    public static String changePassword(String oldPassword, String newPassword){
        if(!Server.getCurrentUser().getPassword().equals(oldPassword)) return "Incorrect password!\n";

        if(!Menu.isPasswordFormatValid(newPassword)) return "Incorrect format for new password!\n";

        Server.getCurrentUser().setPassword(newPassword);
        return "Password changed successfully!\n";
    }

    public static String tellCurrentUsersInfo(){
        String output="";
        User currentUser=Server.getCurrentUser();
        output=output.concat("username: "+currentUser.getUsername()+"\n");
        output=output.concat("password: "+currentUser.getPassword()+"\n");
        output=output.concat("level: "+currentUser.getLevel()+"\n");
        output=output.concat("experience: "+currentUser.getExperience()+"\n");
        output=output.concat("gold: "+currentUser.getGoldAmount()+"\n");
        output=output.concat("rank: "+Server.GetUsersRank(currentUser)+"\n");

        return output;
    }

    public static String removeFromDeck(String cardName){

        if(!Menu.isCardNameValid(cardName)) return "Invalid card name!\n";

        BattleCard targetCard=Server.getCurrentUser().getCardFromDeckByName(cardName);
        if(targetCard==null) 
            return "This card isn't in your battle deck!\n";

        if(Server.getCurrentUser().getDeck().size()==1) 
            return "Invalid action: your battle deck will be empty!\n";

        Server.getCurrentUser().getDeck().remove(targetCard);

        return "Card "+cardName+" removed successfully!\n";
    }

    public static String addToDeck(String cardName){

        if(!Menu.isCardNameValid(cardName)) return "Invalid card name!\n";

        BattleCard targetCard=Server.getCurrentUser().getUsersOwnedCardByName(cardName);
        if(targetCard==null) return "You don't have this card!\n";

        if(Server.getCurrentUser().getCardFromDeckByName(cardName)!= null)
            return "This card is already in your battle deck!\n";
        
        if(Server.getCurrentUser().getDeck().size()==4)
            return "Invalid action: your battle deck is full!\n";


        Server.getCurrentUser().getDeck().add(targetCard);

        return "Card "+cardName+" added successfully!\n";
    }

    public static String showDeck(){
        User currentUser=Server.getCurrentUser();
        String output="";

        for (int i = 1; i < currentUser.getDeck().size()+1; i++) {
            output=output.concat(currentUser.getDeckCardBylexigographicOrder(i).getCardName()+"\n");
        }

        return output;
    }
}
