package Assignment5;

/**
 * Created by walker on 15/12/1.
 */
public class Label {
    private int label;

    public Label() {
        this.label = Integer.MAX_VALUE;
    }

    public Label(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Label)) {
            return false;
        }
        return this.label == ((Label) that).label;
    }

    @Override
    public int hashCode() {
        return label;
    }
}
