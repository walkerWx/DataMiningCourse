package Assignment5;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    public List<Double> getAttributeValues(Attribute attribute) {
        Set<Double> s = new HashSet<>();
        int index = attribute.getAttributeIndex();
        for (int i = 0; i < data.size(); ++i) {
            s.add(data.get(i).get(index));
        }
        List<Double> values = new ArrayList<>(s);
        Collections.sort(values);
        return values;
    }

    public TrainingSet randomSample(int size) {
        TrainingSet randomSet = new TrainingSet();
        List<Double> item;
        Label label;
        for (int i = 0; i < size; ++i) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, data.size());
            item = data.get(randomIndex);
            label = labels.get(randomIndex);
            randomSet.addItem(item, label);
        }
        return randomSet;
    }

    public TrainingSet randomSample() {
        return this.randomSample(this.size());
    }

    public List<TrainingSet> crossValidationPartition(int size) {
        int testSetSize = this.size() / size;

        if (testSetSize == 0) {
            testSetSize = 1;
        }

        Set<Integer> testSetIndexes = new HashSet<>();
        while (testSetIndexes.size() != testSetSize) {
            testSetIndexes.add(ThreadLocalRandom.current().nextInt(0, data.size()));
        }

        TrainingSet trainingSet = new TrainingSet();
        TrainingSet testSet = new TrainingSet();
        for (int i = 0; i < this.size(); ++i) {
            if (testSetIndexes.contains(i)) {
                testSet.addItem(data.get(i), labels.get(i));
            } else {
                trainingSet.addItem(data.get(i), labels.get(i));
            }
        }

        List<TrainingSet> result = new ArrayList<>();
        result.add(trainingSet);
        result.add(testSet);
        return result;
    }

}
