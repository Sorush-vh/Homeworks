public class Message {
    private final String senderId;
   private final Object body;

   public Message( String senderId, Object body) {
        this.senderId = senderId;
       this.body = body;
   }

    public String getSenderId() {
        return senderId;
    }

   public Object getBody() {
       return body;
   }
}
