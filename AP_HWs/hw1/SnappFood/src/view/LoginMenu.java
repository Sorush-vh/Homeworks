package view;
import model.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import controller.LoginMenuController;

public class LoginMenu extends Menu {

    public static User targetUser=null;
    public static boolean keepGoing=true;


    public static void run(Scanner scanner){


        keepGoing=true;
        String registerRegex="\\s*register\\s+(?<username>[^\\s]+)\\s+(?<password>[^\\s]+)\\s*";
        String loginRegex="\\s*login\\s+(?<username>[^\\s]+)\\s+(?<password>[^\\s]+)\\s*";
        String changePasswordRegex="\\s*change\\s+password\\s+(?<username>[^\\s]+)\\s+(?<oldpassword>[^\\s]+)\\s+(?<newpassword>[^\\s]+)\\s*";
        String removeAccountRegex="\\s*remove\\s+account\\s+(?<username>[^\\s]+)\\s+(?<password>[^\\s]+)\\s*";


        String input;
        String output="";

        while (keepGoing) {
            input=scanner.nextLine();
            Matcher resgisterMatcher=Commands.giveTheMatcherStraightUp(input, registerRegex);
            Matcher loginMatcher=Commands.giveTheMatcherStraightUp(input, loginRegex);
            Matcher changePasswordMatcher=Commands.giveTheMatcherStraightUp(input, changePasswordRegex);
            Matcher removeAccountMatcher=Commands.giveTheMatcherStraightUp(input, removeAccountRegex);
            Matcher exitMatcher=Commands.giveTheMatcherStraightUp(input, exitRegex);
            Matcher showMenuTypeMatcher=Commands.giveTheMatcherStraightUp(input, showMenuregex);

            if(resgisterMatcher.matches()){
                String name=resgisterMatcher.group("username");
                String password= resgisterMatcher.group("password");
                output=LoginMenuController.registerCustomer(name,password);
            }
            else if(loginMatcher.matches()){
                String name=loginMatcher.group("username");
                String password= loginMatcher.group("password");
                output=LoginMenuController.login(name, password);
            }
            else if(changePasswordMatcher.matches()){
                String name=changePasswordMatcher.group("username");
                String password= changePasswordMatcher.group("oldpassword");
                String newPassword= changePasswordMatcher.group("newpassword");
                output=LoginMenuController.changePassword(name, password, newPassword);
            }
            else if(removeAccountMatcher.matches()){
                String name=removeAccountMatcher.group("username");
                String password= removeAccountMatcher.group("password");
                output=LoginMenuController.removeAccount(name, password);
            }
            else if(showMenuTypeMatcher.matches()){
                output="login menu"+"\n";
            }
            else if(exitMatcher.matches()){
                return ;
            }
            else output="invalid command!\n";


            System.out.print(output);
            output="";
        }
        keepGoing=true;
        MainMenu.run(targetUser, scanner);
        targetUser=null;
    }



}
