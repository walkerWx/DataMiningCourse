package Assignment5;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by walker on 15/12/1.
 */
public class AttributeSet {
    private List<Attribute> attributes;

    public AttributeSet() {
        this.attributes = new ArrayList<>();
    }

    public AttributeSet(List<Attribute> attributes) {
        this.attributes = new ArrayList<>();
        this.attributes.addAll(attributes);
    }

    public AttributeSet(int n) {
        for (int i = 0; i < n; ++i) {
            attributes.add(new Attribute(i, true));
        }
    }

    public void addAttribute(Attribute attribute) {
        if (!attributes.contains(attribute)) {
            this.attributes.add(attribute);
        }
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public AttributeSet removeAttribute(Attribute attribute) {
        AttributeSet attributeSet = new AttributeSet(this.attributes);
        attributeSet.attributes.remove(attribute);
        return attributeSet;
    }

    public AttributeSet randomSample(int size) {
        AttributeSet randomSet = new AttributeSet();
        int randomIndex;
        while (randomSet.size() != size) {
            randomIndex = ThreadLocalRandom.current().nextInt(0, this.size());
            randomSet.addAttribute(attributes.get(randomIndex));
        }
        return randomSet;
    }

    public AttributeSet randomSample() {
        return this.randomSample((int) (0.5 * this.size()));
    }

    public int size() {
        return attributes.size();
    }

}
