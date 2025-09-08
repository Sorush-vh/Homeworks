import java.util.ArrayList;

public class User {
    private ArrayList<Chat> chats;
    private String id;
    private String name;
    private String password;


    public User( String id, String name, String password){
        chats=new ArrayList<Chat>();
        this.id=id;
        this.name=name;
        this.password=password;
        Messenger.addUser(this);
    }

    public void addChat( Chat chat){
        this.chats.add(chat);
    }

    //ToDo: also add other sorts of chats
    public void addGroup(Group group){
        this.chats.add(group);
    }

    public void addPrivateChat(PrivateChat privateChat){
        this.chats.add(privateChat);
    }

    public void addChannel(Channel channel){
        this.chats.add(channel);
    }

    //ToDo: fill the 3 gettersbyid
    public void getGroupById(String id){

    }

    public String getName(){
        return this.name;
    }

    public String getId(){
        return this.id;
    }

    public String getPassword(){
        return this.password;
    }

    public ArrayList<Chat> getChats(){
        return this.chats;
    }

    public boolean alreadyHavePvChat(String targetId){
        for (Chat chat : chats) {
            if(  chat.findTypeOfChat().equals("private chat")
             && chat.getId().equals(targetId) )
             
            return true;
        }
        return false;
    }

}
