import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Reducer extends Thread {
    public static class WordCountPair {
        private String word;
        private Integer count;

        public WordCountPair(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public Integer getCount() {
            return count;
        }
    }

    LinkedBlockingQueue<WordCountPair> input;
    private HashMap<String, Integer> finalFrequency;

    public Reducer(LinkedBlockingQueue<WordCountPair> input){ 
        this.input=input;
        finalFrequency=new HashMap<>();
    }

    public HashMap<String, Integer> getWordsFrequency() {    
        return finalFrequency;
    }

    private void setFinalFrequency(){
        Iterator<WordCountPair> iterate = input.iterator();
        while (iterate.hasNext()) {

            WordCountPair nextPair=iterate.next();

            if(nextPair==null || nextPair.word==null || nextPair.word.equals("")) return;

            if(finalFrequency.get(nextPair.word) != null)
                finalFrequency.put(nextPair.getWord(), nextPair.count+finalFrequency.get(nextPair.word));

            else finalFrequency.put(nextPair.word,nextPair.count);

            try {
                this.sleep(1000);
                // System.out.println("lol");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        setFinalFrequency();
        // System.out.println("finished");
    }
}