package Assignment5;

import java.util.List;

/**
 * Created by walker on 15/12/6.
 */
public interface Classifier {
    Label classify(List<Double> item);
}
