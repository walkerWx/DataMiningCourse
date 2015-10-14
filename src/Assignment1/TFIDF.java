package Assignment1;

/**
 * Created by walker on 15/10/7.
 */
public class TFIDF {

    public static double calculate(int termFrequency, int totalPosts, int postsWithTerm) {
        double tf = termFrequency;
        double idf = Math.log10((double)totalPosts/postsWithTerm);
        return (tf * idf);
    }


}
