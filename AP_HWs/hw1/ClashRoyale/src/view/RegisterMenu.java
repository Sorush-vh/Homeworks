package view;

import model.*;
import controller.RegisterMenuController;
import controller.Server;

import java.util.Scanner;
import java.util.regex.Matcher;

public class RegisterMenu extends Menu{

    public static boolean keepGoing=true;


    public static void run(Scanner scanner){

        String registerRegex="register username (?<username>.+) password (?<password>.+)";
        String loginRegex="login username (?<username>.+) password (?<password>.+)";
    
        String input,output="";
        while (keepGoing) {

            input=scanner.nextLine();

            Matcher registerMatcher=Commands.giveTheMatcherStraightUp(input, registerRegex);
            Matcher loginMatcher=Commands.giveTheMatcherStraightUp(input, loginRegex);

            if(registerMatcher.matches()){
                String username=registerMatcher.group("username");
                String password=registerMatcher.group("password");
                output=RegisterMenuController.register(username, password);
            }
            else if(loginMatcher.matches()){
                String username=loginMatcher.group("username");
                String password=loginMatcher.group("password");
                output=RegisterMenuController.login(username, password);
            }
            else if(input.equals(showCurrentMenuFormat)){
                output="Register/Login Menu\n";
            }
            else if(input.equals("Exit"))
             return;
            
            else output="Invalid command!\n";

                
            System.out.print(output);
            output="";
        }
        keepGoing=true;
        MainMenu.run(scanner);
    }



 
}
