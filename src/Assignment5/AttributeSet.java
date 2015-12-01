package Assignment5;


import java.util.List;

/**
 * Created by walker on 15/12/1.
 */
public class AttributeSet {
    private List<Integer> attrSet;

    public AttributeSet(List<Integer> attrSet) {
        this.attrSet = attrSet;
    }

    public List<Integer> getAttributes() {
        return this.attrSet;
    }

    public boolean isEmpty() {
        return attrSet.isEmpty();
    }

}
