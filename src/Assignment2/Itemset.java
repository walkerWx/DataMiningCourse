package Assignment2;

/**
 * Created by walker on 15/10/14.
 */
public class Itemset implements Comparable<Itemset> {
    private String content;

    public Itemset(String s){
        content = "";

        s.replaceAll("\\s+","");
        for(int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == '1') {
                content += ('a'+ i);
            }
        }
    }

    @Override
    public int compareTo(Itemset that) {
        return this.content.compareTo(that.content);
    }

    @Override
    public String toString(){
        return this.content;
    }
}
