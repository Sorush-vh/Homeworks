package model;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {
    private String regex;

    public Commands(String regex){
        this.regex=regex;
    }

    public Matcher getMatcher(String input, Commands command){
        Pattern pattern=Pattern.compile(command.regex);
        Matcher matcher=pattern.matcher(input);
        return matcher;
    }

    public static Matcher giveTheMatcherStraightUp(String input, String regexx){
        Commands command= new Commands(regexx);
        Matcher matcher=command.getMatcher(input, command);
        return matcher;
    }
}