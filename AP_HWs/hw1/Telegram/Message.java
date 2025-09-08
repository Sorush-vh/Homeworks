public class Message {
    private User owner;
    private String content;

    public Message(User owner, String content){
        this.owner=owner;
        this.content=content;
    }

    public User getOwner(){
        return this.owner;
    }

    public String getContent(){
        return this.content;
    }

}
