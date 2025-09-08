import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Mapper extends Thread {

    public static Master master;
    

    private String[] words;
    private HashMap<String, Integer> wordsFrequency;

    public Mapper(String input) {
        this.words=input.split(" ");
    }

    public HashMap<String, Integer> getWordsFrequency() { 
        return wordsFrequency;
    }

    public void setWordsFrequency(){

        wordsFrequency=new HashMap<>();


        for (int i = 0; i < words.length; i++) {

            if(words[i].equals("")) continue;

            if(wordsFrequency.get(words[i]) == null)
                wordsFrequency.put(words[i], 1);
            else 
                wordsFrequency.put(words[i], wordsFrequency.get(words[i])+1);

                System.out.println(words[i]);
            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        setWordsFrequency();
        master.fillBlockingQueus(this);
        System.out.println("finished");
    }
}
