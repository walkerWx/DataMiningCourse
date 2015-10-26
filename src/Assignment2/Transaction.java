package Assignment2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/10/14.
 */
public class Transaction implements Comparable<Transaction>{
    private String content;
    private int numOfTotalItems;
    private int numOfContentItems;

    public Transaction(String s){
        content = "";
        s = s.replaceAll("\\s+","");
        numOfTotalItems= s.length();
        for(int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == '1') {
                content += (char)('a'+ i);
            }
        }
        numOfContentItems = content.length();
    }

    public boolean contain(int itemIndex) {
        return content.indexOf((char)('a' + itemIndex)) >= 0 ;
    }

    public boolean containItemset(Itemset itemset) {
        String itemContent = itemset.getContent();
        for (char c : itemContent.toCharArray()) {
            if (content.indexOf(c) < 0) {
                return false;
            }
        }
        return true;
    }

    public int getNumOfTotalItems() {
        return numOfTotalItems;
    }

    public int getNumOfContentItems()  {
        return numOfContentItems;
    }

    public int getItem(int index) {
        return content.charAt(index) - 'a';
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
    public String toString() {
        String s = "";
        for (char c : content.toCharArray()) {
            s += (c - 'a' + 1);
            s += " ";
        }
        return s;
//        return this.content;
    }
}
