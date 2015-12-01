package Assignment5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/12/1.
 */
public class TrainingSet {
    private List<List<Double>> data;

    public TrainingSet(){
        this.data = new ArrayList<>();
    }

    public void addItem(List<Double> item) {
        this.data.add(item);
    }

    public List<Double> getItem(int index) {
        return data.get(index);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }
}
