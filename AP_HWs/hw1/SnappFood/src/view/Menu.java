package view;
import model.Commands;
import java.util.regex.Matcher;

public class Menu {
    private static String type;
    protected static String showMenuregex="\\s*show\\s+current\\s+menu\\s*";
    protected static String exitRegex="\\s*exit\\s*";
    protected static String logoutRegex="\\s*logout\\s*";

    public static boolean isUsernameOrPasswordFormatValid(String username){
        String usernameRegex="[\\_a-zA-Z0-9]+";
        Matcher usernMatcher=Commands.giveTheMatcherStraightUp(username, usernameRegex);
        if(usernMatcher.matches()) return true;
        return false;
    }

    public static boolean isPasswordStrong(String password){
        String bigAlphabetRegex="[A-Z]";
        String smallAlphabetRegex="[a-z]";
        String numberRegex="[0-9]";

        if(password.length()<5) return false;

        Matcher bigAlphabetMatcher=Commands.giveTheMatcherStraightUp(password, bigAlphabetRegex);
        if(!bigAlphabetMatcher.find()) return false;

        Matcher smallAlphabetMatcher=Commands.giveTheMatcherStraightUp(password, smallAlphabetRegex);
        if(!smallAlphabetMatcher.find()) return false;

        Matcher numberMatcher=Commands.giveTheMatcherStraightUp(password, numberRegex);
        if(!numberMatcher.find()) return false;

        return true;
    }

    public static boolean isTypeOrFoodValid(String type){
        String typeFormat="[a-z\\-]+";

        Matcher typeMatcher=Commands.giveTheMatcherStraightUp(type, typeFormat);

        if(!typeMatcher.matches()) return false;
        return true;
    }

    public static boolean isDiscountCodeValid(String code){
        String codeRegex="[a-zA-Z0-9]+";
        Matcher codeMatcher=Commands.giveTheMatcherStraightUp(code, codeRegex);
        if(!codeMatcher.matches()) return false;

        return true;
    }

}
