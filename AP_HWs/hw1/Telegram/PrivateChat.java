public class PrivateChat extends Chat {

    public PrivateChat( String targetId , User currentUser){
        super(currentUser,targetId,Messenger.getUserById(targetId).getName());
    }

    public static void makePrivateChats(String targetId, User currentUser){
        PrivateChat temp= new PrivateChat(targetId, currentUser);
        Messenger.updateChatForTarget(temp, currentUser);

        if(!areIdsDuplicate(targetId, currentUser) )temp.addMember(Messenger.getUserById(targetId));
    }

    private static boolean areIdsDuplicate(String targetId, User currentUser){
        if(currentUser.getId().equals(Messenger.getUserById(targetId).getId())) return true;
        else return false;
    }

    private User getOtherPerson(){
        if(this.getMembers().size()==1) return this.getMembers().get(0);
        if(!this.getMembers().get(0).getId().equals(Messenger.getCurrentUser().getId())){
            return this.getMembers().get(0);
        }
        return this.getMembers().get(1);
    }

    @Override
    public String getId(){
        return this.getOtherPerson().getId();
    }

    @Override
    public String getName(){
        return this.getOtherPerson().getName();
    }
}
