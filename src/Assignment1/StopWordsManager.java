package Assignment1;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by walker on 15/10/6.
 */
public class StopWordsManager {
    private static Set<String> stopWords;

    static {
        stopWords = new HashSet<String>();
        File stopWordsFile = new File("/Users/walker/Desktop/Courses/DataMining/Assignments/1/Chinese-stop-words.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsFile), "cp936"))) {
            String word;
            while ((word = br.readLine()) != null) {
                stopWords.add(word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getStopWords() {
        return stopWords;
    }
}
