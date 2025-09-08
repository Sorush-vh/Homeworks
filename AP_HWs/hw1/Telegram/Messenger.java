import java.util.ArrayList;

public class Messenger {
    
    private static ArrayList<Channel> channels;
    private static ArrayList<Group> groups;
    private static ArrayList<User> users;
    private static User currentUser;

    static{
        channels= new ArrayList<Channel>();
        groups= new ArrayList<Group>();
        users= new ArrayList<User>();
    }


    public static void updateChatForTarget(Chat targetChat, User targetUser){
        int targetIndex=targetUser.getChats().indexOf(targetChat);
        targetUser.getChats().remove(targetIndex);
        targetUser.getChats().add(0, targetChat);
    }

    public static void setCurrentUser(User user){
        currentUser=user;
    }

    public static void addGroup(Group group){
        groups.add(group);
    }

    public static void addChannel(Channel channel){
        channels.add(channel);
    }

    public static void addUser(User user){
        users.add(user);
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static ArrayList<Channel> getChannels(){
        return channels;
    }

    public static ArrayList<Group> getGroups(){
        return groups;
    }

    public static User getUserById(String id){
        for (User user : users) {
            if(user.getId().equals(id)) return user;
        }
        return null;
    }

    public static Channel getChannelById(String id){
        for (Channel channel : channels) {
            if(channel.getId().equals(id)) return channel;
        }
        return null;
    }

    public static Group getGroupById(String id){
        for (Group group : groups) {
            if(group.getId().equals(id)) return group;
        }
        return null;
    }
}
