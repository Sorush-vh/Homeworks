package view;

import model.*;
import controller.Server;

import java.util.regex.Matcher;

public class Menu {
    
    public static String showCurrentMenuFormat="show current menu";

    public static boolean isCardNameValid(String cardName){
        String regex="(Fireball)|(Heal)|(Barbarian)|(Baby Dragon)|(Ice Wizard)";
        Matcher matcher=Commands.giveTheMatcherStraightUp(cardName, regex);
        if(matcher.matches()) return true;
        return false;
    }

    public static boolean isUsernameValid(String username){

        String correctRegex="[a-zA-Z]+";
        Matcher matcher=Commands.giveTheMatcherStraightUp(username, correctRegex);

        if(matcher.matches()) return true;
        return false;
    }

    public static boolean isPasswordFormatValid(String password){
        if(password.length()>20 || password.length()<8) return false;

        if(password.contains(" ")) return false;

        if(!DoesContainBigLetter(password)) return false;

        if(!DoesContainSmallLetter(password)) return false;

        if(!DoesContainSpecialLetter(password)) return false;
 
        if(!DoesContainNumbers(password)) return false;

        if(DoesStartWithNumber(password)) return false;

        return true;
    }

    public static boolean DoesContainBigLetter(String target){
        String bigAlphabetRegex="[A-Z]";
        Matcher bigAlphabetMatcher=Commands.giveTheMatcherStraightUp(target, bigAlphabetRegex);
        if(bigAlphabetMatcher.find()) return true;
        return false;
    }

    public static boolean DoesContainSmallLetter(String target){
        String smallAlphabetRegex="[a-z]";
        Matcher smallAlphabetMatcher=Commands.giveTheMatcherStraightUp(target, smallAlphabetRegex);
        if(smallAlphabetMatcher.find()) return true;
        return false;
    }

    public static boolean DoesContainSpecialLetter(String target){
        String specialRegex="[\\!\\@\\#\\$\\%\\^\\&\\*]";
        Matcher specialMatcher=Commands.giveTheMatcherStraightUp(target, specialRegex);
        if(specialMatcher.find()) return true;
        return false;
    }

    public static boolean DoesContainNumbers(String target){
        String numberRegex="[0-9]";
        Matcher numbMatcher=Commands.giveTheMatcherStraightUp(target, numberRegex);
        if(numbMatcher.find()) return true;
        else return false;
    }

    public static boolean DoesStartWithNumber(String target){
        String regex="[0-9].+";
        Matcher matcher=Commands.giveTheMatcherStraightUp(target, regex);
        if(matcher.matches()) return true;
        else return false;
    }
}
