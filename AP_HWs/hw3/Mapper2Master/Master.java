import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Master {

    private Reducer reducer1, reducer2;
    private Mapper mapper1, mapper2, mapper3, mapper4;
    LinkedBlockingQueue<Reducer.WordCountPair> queue1;
    LinkedBlockingQueue<Reducer.WordCountPair> queue2;

    private String[] lineInputs;

    public Master(String input) throws InterruptedException {
        Mapper.master=this;
        lineInputs = input.split("\n");
        initializeMappers();
        queue1=new LinkedBlockingQueue<>();
        queue2= new LinkedBlockingQueue<>();
    }

    private void initializeMappers() {
        String input1 = "", input2 = "", input3 = "", input4 = "";

        for (int i = 0; i < lineInputs.length; i++) {
            switch (i % 4) {
                case 0:
                    input1 = input1.concat(lineInputs[i]);
                    break;
                case 1:
                    input2 = input2.concat(lineInputs[i]);
                    break;
                case 2:
                    input3 = input3.concat(lineInputs[i]);
                    break;
                default:
                    input4 = input4.concat(lineInputs[i]);
                    break;
            }
        }

        mapper1 = new Mapper(input1);
        mapper2 = new Mapper(input2);
        mapper3 = new Mapper(input3);
        mapper4 = new Mapper(input4);
    }

    public static int getWordHashcodeFormat(String word) {
        return word.hashCode() % 2;
    }

    public HashMap<String, Integer> getWordsFrequency() throws InterruptedException {
        mapper1.start();
        mapper2.start();
        mapper3.start();
        mapper4.start();
        mapper1.join();
        mapper2.join();
        mapper3.join();
        mapper4.join();

        reducer1=new Reducer(queue1);
        reducer2=new Reducer(queue2);

        reducer1.start();
        reducer2.start();
        reducer1.join();
        reducer2.join();

        HashMap<String,Integer> output=new HashMap<>();
        output.putAll(reducer1.getWordsFrequency());
        output.putAll(reducer2.getWordsFrequency());

        return output;
    }

    public void fillBlockingQueus(Mapper mapper) {
        HashMap<String , Integer> wordsDataFromMapper=mapper.getWordsFrequency();
        
        for (String word : wordsDataFromMapper.keySet()) {
            if(getWordHashcodeFormat(word)==0)
                queue1.add(new Reducer.WordCountPair(word, wordsDataFromMapper.get(word)));
            else 
                queue2.add(new Reducer.WordCountPair(word, wordsDataFromMapper.get(word)));
        }
    }
}