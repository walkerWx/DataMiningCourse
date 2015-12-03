package Assignment5;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/12/1.
 */
public class AttributeSet {
    private List<Attribute> attributes;

    public AttributeSet() {
        this.attributes = new ArrayList<>();
    }

    public AttributeSet(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public AttributeSet(int n) {
        for (int i = 0; i < n; ++i) {
            attributes.add(new Attribute(i, true));
        }
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public AttributeSet removeAttribute(Attribute attribute) {
        AttributeSet attributeSet= new AttributeSet(this.attributes);
        attributeSet.attributes.remove(attribute);
        return attributeSet;
    }

}
