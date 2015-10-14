package Assignment2;

/**
 * Created by walker on 15/10/14.
 */
public class Itemset implements Comparable<Itemset> {
    private String content;

    public Itemset() {
        this.content = null;
    }

    public Itemset(String s) {
        content = "";

        s.replaceAll("\\s+", "");
        for (int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == '1') {
                content += ('a' + i);
            }
        }
    }

    public Itemset(int i) {
        content = Character.toString((char) ('a' + i));
    }

    public static Itemset generateCandidate(Itemset s1, Itemset s2) {
        if (s1.content.length() == 0 || s1.content.length() != s2.content.length() || s1.compareTo(s2) == 0) {
            return null;
        }

        if (s1.content.substring(0, s1.content.length() - 1).equals(s2.content.substring(0, s2.content.length() - 1))) {
            Itemset candidate = new Itemset();
            if (s1.compareTo(s2) > 0) {
                candidate.content = s2.content + s1.content.substring(s1.content.length() - 1);
            } else {
                candidate.content = s1.content + s2.content.substring(s2.content.length() - 1);
            }
            return candidate;
        }

        return null;
    }


    @Override
    public int compareTo(Itemset that) {
        return this.content.compareTo(that.content);
    }

    @Override
    public String toString() {
        return this.content;
    }
}
