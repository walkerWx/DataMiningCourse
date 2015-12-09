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

    public List<TrainingSet> crossValidationPartition(int nPartition) {

        int setSize = this.size() / nPartition;
        if (setSize == 0) {
            setSize = 1;
        }

        Set<Integer> randomIndexes;

        List<List<Double>> dataCopy = new ArrayList<>();
        dataCopy.addAll(data);
        List<Label> labelsCopy = new ArrayList<>();
        labelsCopy.addAll(labels);

        List<TrainingSet> partition = new ArrayList<>();
        TrainingSet ts;
        for (int i = 0; i < nPartition - 1; ++i) {
            int randomIndex;
            ts = new TrainingSet();
            for (int j = 0; j < setSize; ++j) {
                randomIndex = ThreadLocalRandom.current().nextInt(0, dataCopy.size());
                ts.addItem(dataCopy.remove(randomIndex), labelsCopy.remove(randomIndex));
            }
            partition.add(ts);
        }

        // the remaining part
        ts = new TrainingSet();
        ts.data.addAll(dataCopy);
        ts.labels.addAll(labelsCopy);
        partition.add(ts);

        return partition;
    }

    public void append(TrainingSet trainingSet) {
        this.data.addAll(trainingSet.data);
        this.labels.addAll(trainingSet.labels);
    }

    public TrainingSet randomSample(int size) {
        TrainingSet result = new TrainingSet();
        int randomIndex;
        for (int i = 0; i < size; ++i) {
            randomIndex = ThreadLocalRandom.current().nextInt(this.size());
            result.addItem(data.get(randomIndex), labels.get(randomIndex));
        }
        return result;
    }

    public TrainingSet randomSample() {
        return randomSample(this.size());
    }

    public TrainingSet weightedSample(List<Double> weights) {
        List<Double> intervals = new ArrayList<>();
        double current = 0.0;
        for (int i = 0; i < weights.size(); ++i) {
            current += weights.get(i);
            intervals.add(current);
        }

        double random;
        TrainingSet result = new TrainingSet();
        for (int i = 0; i < this.size(); ++i) {
            random = ThreadLocalRandom.current().nextDouble(current);
            for (int j = 0; j < intervals.size(); ++j) {
                if (intervals.get(j) > random) {
                    result.addItem(data.get(j), labels.get(j));
                    break;
                }
                continue;
            }
        }
        return result;
    }
}
