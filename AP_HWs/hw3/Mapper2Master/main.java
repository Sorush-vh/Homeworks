public class main {
    
    public static void main(String[] args) throws InterruptedException {
        Master master=new Master("w1 w2 w3 w4\n w1 w2 w3\n w1 w2 w5\n w4 w4 w2");
        System.out.println(master.getWordsFrequency());
    }
}
