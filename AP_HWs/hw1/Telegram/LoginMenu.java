import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class LoginMenu{

    public static void run(Scanner scanner){
        String loginFormat="login i (?<id>[^\\s]+) p (?<password>[^\\s]+)";
        String registerFormat="register i (?<id>[^\\s]+) u (?<username>[^\\s]+) p (?<password>[^\\s]+)";




        String input;
        boolean EnterMessengerMenu=false;
        String output="";
        User targetUser=null;

        while(true){
            input=scanner.nextLine();
            Matcher loginMatcher=Commands.giveTheMatcherStraightUp(input, loginFormat);
            Matcher registerMatcher=Commands.giveTheMatcherStraightUp(input, registerFormat);

            if(input.equals("exit"))
            return ;

            else if(registerMatcher.matches()) 
                output= register(registerMatcher);

            else if(loginMatcher.matches()) {
                output=login(loginMatcher);
                targetUser=findValidloginTargetUser(loginMatcher);
                if(targetUser != null) EnterMessengerMenu=true;
            }
            else 
                output="Invalid command!";

            System.out.println(output);
            output="";

            if (EnterMessengerMenu) break;
        }
        Messenger.setCurrentUser(targetUser);
        MessengerMenu.run(scanner);
    }

    private static String login(Matcher matcher){
        String id=matcher.group("id");
        String password=matcher.group("password");
        User user=Messenger.getUserById(id);

        if(user==null) return "No user with this id exists!";

        if(!user.getPassword().equals(password)) return "Incorrect password!";

        return "User successfully logged in!";
    }

    private static User findValidloginTargetUser(Matcher matcher){
        String id=matcher.group("id");
        String password=matcher.group("password");
        User user=Messenger.getUserById(id);

        if(user==null) return null;

        if(!user.getPassword().equals(password)) return null;

        return user;
    }

    private  static String register(Matcher matcher){
        //ToDo: having space should always be invalid
        if (!isUsernameValid(matcher.group("username")))  
            return "Username's format is invalid!";
        

        if (!isPasswordStrong(matcher.group("password")))
            return "Password is weak!";
        

        if (Messenger.getUserById(matcher.group("id")) != null)
            return "A user with this ID already exists!";
        

        User temp=new User(matcher.group("id"), matcher.group("username"),matcher.group("password"));
        return "User has been created successfully!";
    }

    private static boolean isPasswordStrong(String password){
       // *.!@$%^&(){}[]:;<>,?/~_+-=|
        if (password.length()>32 || password.length()<8) return false;


        String weirdFormat="[\\*\\.\\!\\@\\$\\%\\^\\&\\(\\)\\{\\}\\[\\]\\:\\;\\<\\>\\,\\?\\/\\~\\_\\+\\-\\=\\|]";
        Pattern weirdPattern=Pattern.compile(weirdFormat);
        Matcher weirdMatcher=weirdPattern.matcher(password);
        if(!weirdMatcher.find()) return false;


        String number="[0-9]";
        Pattern numberPattern=Pattern.compile(number);
        Matcher numberMatcher=numberPattern.matcher(password);
        if(!numberMatcher.find()) return false;


        String smallAlphabet="[a-z]";
        Pattern smallAlphabetPattern=Pattern.compile(smallAlphabet);
        Matcher smallAlphabetMatcher=smallAlphabetPattern.matcher(password);
        if(!smallAlphabetMatcher.find()) return false;

        String BigAlphabet="[A-Z]";
        Pattern BigAlphabetPattern=Pattern.compile(BigAlphabet);
        Matcher BigAlphabetMatcher=BigAlphabetPattern.matcher(password);
        if(!BigAlphabetMatcher.find()) return false;

        return true;        
    }

    private static boolean isUsernameValid(String username){
        String usernameFormat="[a-zA-Z0-9\\_]+";
        Pattern usernamePattern=Pattern.compile(usernameFormat);
        Matcher usernameMatcher=usernamePattern.matcher(username);
        if(usernameMatcher.matches()) return true;
        else return false;
    }

}