import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;


public class MessengerMenu{

    public static void run(Scanner scanner){
        String input;
        String output="";

        String createChannelFormat="create new channel i (?<id>[^\\s]+) n (?<name>[^\\s]+)";
        String createGroupFormat="create new group i (?<id>[^\\s]+) n (?<name>[^\\s]+)";
        String startPrivateChatFormat="start a new private chat with i (?<id>[^\\s]+)";
        String joinChannelFormat="join channel i (?<channelId>[^\\s]+)";
        String enterChatFormat="enter (?<chatType>(private chat)|(group)|(channel)) i (?<id>[^\\s]+)";
        

        boolean keepGoing=true;
        Chat targetChat=null;

        while (keepGoing) {
            input=scanner.nextLine();
            Matcher createChannelMatcher=Commands.giveTheMatcherStraightUp(input, createChannelFormat);
            Matcher createGroupMatcher=Commands.giveTheMatcherStraightUp(input, createGroupFormat);
            Matcher startPrivateChatMatcher=Commands.giveTheMatcherStraightUp(input, startPrivateChatFormat);
            Matcher joinChannelMatcher=Commands.giveTheMatcherStraightUp(input, joinChannelFormat);
            Matcher enterChatMatcher=Commands.giveTheMatcherStraightUp(input, enterChatFormat);
            
            if(input.equals("logout")){
                output="Logged out";
                keepGoing=false;
                output=output.concat("\n");
            }
            

            else if(input.equals("show all channels"))
            output=showAllChannels();

            else if(input.equals("show my chats"))
            output=showChats();

            else if(createChannelMatcher.matches()){
                output=createChannel(createChannelMatcher);
                output=output.concat("\n");
            }
            
            else if(createGroupMatcher.matches()){
                output=createGroup(createGroupMatcher);
                output=output.concat("\n");
            }
            
            else if(startPrivateChatMatcher.matches()){
                output=startPvChat(startPrivateChatMatcher);
                output=output.concat("\n");
            }

            else if(joinChannelMatcher.matches()){
                output=joinChannel(joinChannelMatcher);
                output=output.concat("\n");
            }
                
            else if(enterChatMatcher.matches()){
                output=startChat(enterChatMatcher);
                output=output.concat("\n");
                targetChat=getChat(enterChatMatcher);
                if(targetChat != null) keepGoing=false;
            }
            else {
            output="Invalid command!";
            output=output.concat("\n");
            }


            System.out.print(output);
            output="";

            if(!keepGoing) break;
        }
        if(targetChat != null) {
            ChatMenu.run(scanner, targetChat);
            return ;
        }
        LoginMenu.run(scanner);
    }

    private static String showAllChannels(){
        String output="";
        output=output.concat("All channels:\n");
        int i=1;

        for (Channel channel : Messenger.getChannels()) {

            output=output.concat(""+i+". ");
            output=output.concat(channel.getName()+", ");
            output=output.concat("id: "+channel.getId()+", ");
            output=output.concat("members: "+channel.getMembers().size());
            output=output.concat("\n");

            i++;
        }
        return output;
    }
    
    private static String showChats(){
        //ToDo: sort in order
        String output="Chats:\n";
        int i=1;
        for (Chat chat : Messenger.getCurrentUser().getChats()) {
            output=output.concat(""+i+". ");
            output=output.concat(chat.getName()+", ");
            output=output.concat("id: "+chat.getId()+", ");
            output=output.concat(chat.findTypeOfChat());
            output=output.concat("\n");

            i++;
        }
        return output;
    }

    private static String createChannel(Matcher matcher){
        String id=matcher.group("id");
        String name=matcher.group("name");
        return createChannelWithExactData(id, name);
    }

    private static String createChannelWithExactData(String id, String channelName){

        if(!isChannelNameValid(channelName)) return "Channel name's format is invalid!";
        if(Messenger.getChannelById(id)!=null) return "A channel with this id already exists!";

        Channel temp=new Channel(Messenger.getCurrentUser(),id,channelName);
        String output="Channel "+ temp.getName()+" has been created successfully!";
        return output;
    }

    private static String createGroup(Matcher matcher){
        String id=matcher.group("id");
        String name=matcher.group("name");
        return createGroupWithExactData(id, name);
    }

    private static String createGroupWithExactData(String id, String groupName){
        if(!isChannelNameValid(groupName)) return "Group name's format is invalid!";

        if(Messenger.getGroupById(id) != null) return "A group with this id already exists!";

        Group temp= new Group(Messenger.getCurrentUser(), id, groupName);
        return "Group "+temp.getName()+" has been created successfully!";
    }

    private static String startPvChat(Matcher matcher){
        String id=matcher.group("id");
        return createPrivateChatWithExatctData(id);
    }

    private static String createPrivateChatWithExatctData(String targetId){
        if(Messenger.getUserById(targetId) == null) return "No user with this id exists!";

        if(Messenger.getCurrentUser().alreadyHavePvChat(targetId))
         return "You already have a private chat with this user!";

        PrivateChat.makePrivateChats(targetId, Messenger.getCurrentUser()); 
        return "Private chat with " +Messenger.getUserById(targetId).getName()+ " has been started successfully!";
    }

    private static String joinChannel(Matcher matcher){
        String channelId=matcher.group("channelId");
        return joinChannelWithExactData(channelId);
    }

    private static String joinChannelWithExactData(String channelId){
        Channel channel= Messenger.getChannelById(channelId);

        if(channel==null) return "No channel with this id exists!";

        if(channel.getMemberById(Messenger.getCurrentUser().getId()) != null) 
         return "You're already a member of this channel!";

        channel.addMember(Messenger.getCurrentUser());
        return "You have successfully joined the channel!";

    }

    private static boolean isChannelNameValid(String name){
        String formula="[a-zA-Z0-9\\_]+";
        Matcher matcher=Commands.giveTheMatcherStraightUp(name, formula);
        if(!matcher.matches()) return false;
        return true;
    }

    private static String startChat(Matcher matcher){
        String chatType=matcher.group("chatType");
        String id=matcher.group("id");
        return startChatWithExactData(chatType, id);
    }

    private static String startChatWithExactData(String ChatType, String id){
        Chat targetChat=null;
        for (Chat chat : Messenger.getCurrentUser().getChats()) {
            if(chat.getId().equals(id) &&
            chat.findTypeOfChat().equals(ChatType) )
            targetChat=chat;
        }
        if(targetChat == null ) return "You have no "+ChatType+" with this id!";

        return "You have successfully entered the chat!";
    }

    private static Chat getChat(Matcher matcher){
        String chatType=matcher.group("chatType");
        String id=matcher.group("id");

        Chat targetChat=null;
        for (Chat chat : Messenger.getCurrentUser().getChats()) {
            if(chat.getId().equals(id) &&
            chat.findTypeOfChat().equals(chatType) )
            targetChat=chat;
        }

        return targetChat;
    }
}