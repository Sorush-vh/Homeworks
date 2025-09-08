package view;

import model.*;
import controller.Server;
import controller.ShopMenuController;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ShopMenu extends Menu {
    
    public static ArrayList<BattleCard> shop;
    private static boolean keepGoing=true;

    static{
        shop=new ArrayList<BattleCard>();
        fillShop();
    }

    public static void run(Scanner scanner){

        String buyCardRegex="buy card (?<cardName>.+)";
        String sellCardRegex="sell card (?<cardName>.+)";

        String input,output="";

        while (keepGoing) {
            input=scanner.nextLine();

            Matcher buyCardMatcher=Commands.giveTheMatcherStraightUp(input, buyCardRegex);
            Matcher sellCardMatcher=Commands.giveTheMatcherStraightUp(input, sellCardRegex);

            if(buyCardMatcher.matches()){
                String cardName=buyCardMatcher.group("cardName");
                output=ShopMenuController.buyCard(cardName);
            }
            else if(sellCardMatcher.matches()){
                String cardName=sellCardMatcher.group("cardName");
                output=ShopMenuController.sellCard(cardName);
            }
            else if(input.equals(showCurrentMenuFormat)){
                output="Shop Menu\n";
            }
            else if(input.equals("back")){
                output="Entered main menu!\n";
                keepGoing=false;
            }
            else output="Invalid command!\n";

            System.out.print(output);
            output="";
        }
        keepGoing=true;
        MainMenu.run(scanner);
    }


    private static void fillShop(){
        Troops barbarian=Troops.makeBarbarian();
        shop.add(barbarian);

        Troops iceWizard=Troops.makeIceWizard();
        shop.add(iceWizard);
        
        Troops babyDragon=Troops.makeBabyDragon();
        shop.add(babyDragon);

        Spells fireball=Spells.makeFireball();
        shop.add(fireball);

        Spells heal=Spells.makeHealSpell();
        shop.add(heal);
    }

}
