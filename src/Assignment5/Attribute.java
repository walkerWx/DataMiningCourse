package Assignment5;

/**
 * Created by walker on 15/12/1.
 */
public class Attribute {
    private int attributeIndex;
    private boolean isDiscrete;

    public Attribute() {
        this.attributeIndex = -1;
        this.isDiscrete = true;
    }

    public Attribute(int attributeIndex, boolean isDiscrete) {
        this.attributeIndex = attributeIndex;
        this.isDiscrete = isDiscrete;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public boolean isDiscrete() {
        return isDiscrete;
    }
}
