package Assignment2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/10/14.
 */
public class Transaction implements Comparable<Transaction>{
    private String content;
    private int numOfItems;

    public Transaction(String s){
        content = "";
        s = s.replaceAll("\\s+","");
        numOfItems = s.length();
        for(int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == '1') {
                content += (char)('a'+ i);
            }
        }
    }

    public boolean contain(int itemIndex) {
        return content.indexOf((char)('a' + itemIndex)) >= 0 ;
    }

    public List<Integer> items() {
        ArrayList<Integer> items = new ArrayList<>();
        for (char c : content.toCharArray()) {
            items.add(new Integer(c - 'a'));
        }
        return items;
    }

    @Override
    public int compareTo(Transaction that){
        return this.content.compareTo(that.content);
    }

    @Override
    public String toString(){
        return this.content;
    }
}
