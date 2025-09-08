import java.util.ArrayList;

public class Chat {
    private ArrayList<User> members;
    private ArrayList<Message> messages;
    private User owner;
    private String id;
    private String name;

    public Chat( User owner, String id, String name){
        members= new ArrayList<User>();
        messages= new ArrayList<Message>();
        this.owner=owner;
        this.id=id;
        this.name=name;
        this.members.add(owner);
        owner.addChat(this);
        Messenger.updateChatForTarget(this, owner);
    }

    public void addMember(User user){
        this.members.add(user);
        user.addChat(this);
        Messenger.updateChatForTarget(this, user);
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
    
    public User getOwner(){
        return this.owner;
    }

    public String getName(){
        return this.name;
    }

    public String getId(){
        return this.id;
    }

    public ArrayList<Message> getMessages(){
        return this.messages;
    }

    public ArrayList<User> getMembers(){
        return this.members;
    }

    public User getMemberById(String id){
        for (User user : members) {
            if(user.getId().equals(id)) return user;
        }
        return null;
    }

    public String findTypeOfChat(){
        if(this instanceof Channel) return "channel";
        if(this instanceof PrivateChat) return "private chat";
         return "group";
    }

    public boolean isCurrentUserOwner(){
        if(Messenger.getCurrentUser().getId().equals(this.getOwner().getId()))
        return true;
        return false;
    }


}
