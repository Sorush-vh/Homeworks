public class Channel extends Chat {
    public Channel(User owner , String id, String name){
        super(owner, id, name);
        Messenger.addChannel(this.);

    }
}
