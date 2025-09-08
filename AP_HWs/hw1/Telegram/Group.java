public class Group extends Chat {
    public Group(User owner, String id, String name){
        super(owner, id, name);
        Messenger.addGroup(this);
    }
}
