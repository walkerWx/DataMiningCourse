package Assignment4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by walker on 15/11/16.
 */
public class HingeLoss {
    public static void main(String[] args) {
//        InputReader ir = new InputReader("/Users/walker/Desktop/DataMining/dataset1-a8a-training.txt");
        InputReader ir = new InputReader("/Users/walker/Desktop/DataMining/dataset1-a9a-training.txt");
        List<List<Integer>> data = ir.getData();
        List<Integer> label = ir.getLabel();
        int iteration = 5 * ir.getScale();

//        ir = new InputReader("/Users/walker/Desktop/DataMining/dataset1-a8a-testing.txt");
        ir = new InputReader("/Users/walker/Desktop/DataMining/dataset1-a9a-testing.txt");
        List<List<Integer>> testData = ir.getData();
        List<Integer> testLabel = ir.getLabel();

        // Parameters
//        double lamda = 1e-4;
        double lamda = 5e-5;

        // initialize solution vector
        List<Double> w = new ArrayList<>();
        for (int i = 0; i < ir.getFeatureSize(); ++i) {
            w.add(.0);
        }

        Random r = new Random();
        for (int t = 1; t <= 10; ++t) {
            for (int i = 0; i < iteration * t / 10; ++i) {
                int randomIndex = r.nextInt(data.size());
                List<Integer> randomTrainingData = data.get(randomIndex);
                int randomLabel = label.get(randomIndex);

                int indicator = indicatorFunction(randomLabel, w, randomTrainingData);
                for (int j = 0; j < w.size(); ++j) {
                    w.set(j, (1 - 1.0 / (i + 1)) * w.get(j) + indicator * 1.0 / (lamda * (i + 1)) * randomLabel * randomTrainingData.get(j));
                }
            }
//        w.forEach(e -> System.out.print(e + " "));

            int errorCount = 0;
            for (int i = 0; i < testData.size(); ++i) {
                if (innerProduct(testData.get(i), w) * testLabel.get(i) < 0) {
                    errorCount++;
                }
            }
            System.out.print(t/10.0 + " ");
            System.out.println(errorCount * 1.0 / testData.size());
        }
    }

    private static int indicatorFunction(int y, List<? extends Number> w, List<? extends Number> x) {
        assert w.size() == x.size();
        return y * innerProduct(w, x) < 1 ? 1 : 0;
    }

    private static double innerProduct(List<? extends Number> a, List<? extends Number> b) {
        assert a.size() == b.size();
        double product = 0;
        for (int i = 0; i < a.size(); ++i) {
            product += a.get(i).doubleValue() * b.get(i).doubleValue();
        }
        return product;
    }
}
