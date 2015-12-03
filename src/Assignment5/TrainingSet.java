package Assignment5;

import java.util.*;

/**
 * Created by walker on 15/12/1.
 */
public class TrainingSet {
    private List<List<Double>> data;
    private List<Label> labels;

    public TrainingSet() {
        this.data = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    public void addItem(List<Double> item, Label label) {
        this.data.add(item);
        this.labels.add(label);
    }

    public List<Double> getItem(int index) {
        return data.get(index);
    }

    public List<Label> getLabels() {
        return this.labels;
    }

    public Label getLabel(int index) {
        return labels.get(index);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public boolean isAllSameLabel() {
        for (int i = 0; i < labels.size(); ++i) {
            if (labels.get(i).getLabel() != labels.get(0).getLabel()) {
                return false;
            }
        }
        return true;
    }

    public int attributeSize() {
        if (!data.isEmpty()) {
            return this.data.get(0).size();
        }
        return 0;
    }

    public List<Double> getAttributeValues(Attribute attribute){
        Set<Double> s = new HashSet<>();
        int index = attribute.getAttributeIndex();
        for (int i = 0; i < data.size(); ++i) {
            s.add(data.get(i).get(index));
        }
        List<Double> values = new ArrayList<>(s);
        Collections.sort(values);
        return values;
    }


}
