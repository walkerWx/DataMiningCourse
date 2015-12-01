package Assignment5;

import java.util.List;

/**
 * Created by walker on 15/12/1.
 */
public class LabelSet {

    private List<Label> labels;

    public List<Label> getLabels(){
        return labels;
    }

    public boolean allSame() {

        for (int i = 0; i < labels.size(); ++i) {
            if (labels.get(i).getLabel() != labels.get(0).getLabel()) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        return labels.size();
    }
}
