import java.util.Scanner;
import java.util.regex.Matcher;

public class ChatMenu {
    private static Chat chat;
    
    public static void run(Scanner scanner , Chat targetChat){
        chat=targetChat;
        String input;
        String output="";
        String addMemberFormat="add member i (?<id>[^\\s]+)";
        String sendMessafeFormat="send a message c (?<message>.+)";

        while (true) {
            input=scanner.nextLine();
            Matcher addMemberMatcher=Commands.giveTheMatcherStraightUp(input, addMemberFormat);
            Matcher sendMessageMatcher=Commands.giveTheMatcherStraightUp(input, sendMessafeFormat);

            if(input.equals("back")) break;

            if(input.equals("show all members"))
                output=showMembers();

            else if(input.equals("show all messages"))
                output=showMessages();

            else if(addMemberMatcher.matches()){
                output=addMemberWithExactData(addMemberMatcher.group("id"));
                output= output.concat("\n");
            }
            
            else if(sendMessageMatcher.matches()){
                output=sendMessageWithExactData(sendMessageMatcher.group("message"));
                output= output.concat("\n");
            }
            
            else{
                output="Invalid command!";
                output= output.concat("\n");
            }

            System.out.print(output);
            output="";
        }
        MessengerMenu.run(scanner);
    }  

    private static String sendMessageWithExactData(String content){

        Message message=new Message(Messenger.getCurrentUser(), content);

        if(chat.findTypeOfChat().equals("channel") &&
         !chat.isCurrentUserOwner()) 
        return "You don't have access to send a message!";

        chat.addMessage(message);
        updateChatOrderForMembers(chat);

        return "Message has been sent successfully!";
    }

    private static void updateChatOrderForMembers(Chat targetChat){

        for (User member : targetChat.getMembers()) {
            Messenger.updateChatForTarget(targetChat, member);;
        }
    }

    private static String addMemberWithExactData(String id){

        if( chat.findTypeOfChat().equals("private chat") ) 
         return "Invalid command!";

        if(!chat.isCurrentUserOwner()) return "You don't have access to add a member!";

        if(Messenger.getUserById(id) == null) return "No user with this id exists!";

        if(chat.getMemberById(id) != null) return "This user is already in the chat!";




        chat.addMember(Messenger.getUserById(id));
        Messenger.updateChatForTarget(chat, Messenger.getUserById(id));

        if(chat.findTypeOfChat().equals("group"))
        sendMessageWithExactData(Messenger.getUserById(id).getName()+" has been added to the group!");

        return "User has been added successfully!";
    }

    private static String showMessages(){
        String output="Messages:\n";
        for (Message message : chat.getMessages()) {
            output=output.concat(message.getOwner().getName()+"("+message.getOwner().getId()+"): ");
            output=output.concat("\""+message.getContent()+"\"");
            output=output.concat("\n");
        }
        return output;
    }

    private static String showMembers(){
        if(chat.findTypeOfChat().equals("private chat")) return "Invalid command!\n";

        String output="Members:\n";
        for (User member : chat.getMembers()) {
            output=output.concat("name: "+member.getName()+", ");
            output=output.concat("id: "+member.getId());

            if(chat.getOwner().getId().equals(member.getId()))
            output=output.concat(" *owner");

            output=output.concat("\n");
        }


        return output; 
    }
}
