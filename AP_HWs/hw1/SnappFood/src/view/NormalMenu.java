package view;
import model.*;
import java.util.regex.Matcher;

public class NormalMenu extends Menu {

    protected static String chargeBalanceRegex="\\s*charge\\s+account\\s+(?<amount>\\-?[0-9]+)\\s*";
    protected static String showBalanceregex="\\s*show\\s+balance\\s*";

    public static String chargeBalance(NormalUsers user, int charge){
        if(charge<=0) return "charge account failed: invalid cost or price\n";
        
        user.addBalance(charge);
        return "charge account successful\n";
    }

    public static String showBalance(NormalUsers user){
        return ""+user.getBalance()+"\n";
    }

    public static boolean isFoodCategoryValid(String category){
        String categoryFormat="(dessert)|(entree)|(starter)";
        Matcher categoryMatcher=Commands.giveTheMatcherStraightUp(category, categoryFormat);
        if(!categoryMatcher.matches()) return false;
        return true;
    }
}
