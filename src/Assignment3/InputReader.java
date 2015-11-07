package Assignment3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/11/7.
 */
public class InputReader {

    List<Point> points;
    int nFeatures;

    public InputReader(String path) {
        File data = new File(path);
        points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "utf-8"))) {
            String line;
            int id = 0;
            while ((line = br.readLine()) != null) {
                String[] featuresString = line.split(",");
                nFeatures= featuresString.length - 1; // The last column represents the label of the corresponding example
                ArrayList<Double> features = new ArrayList<>();
                for (int i = 0; i < nFeatures; ++i) {
                    features.add(Double.parseDouble(featuresString[i]));
                }
                int label = Integer.parseInt(featuresString[featuresString.length - 1]);
                points.add(new Point(id++, features, label));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public double[][] get2dArray() {
        double[][] featuresArray = new double[points.size()][nFeatures];
        for (int i = 0; i < points.size(); ++i) {
            for (int j = 0; j < nFeatures; ++j) {
                featuresArray[i][j] = points.get(i).getFeatures().get(j);
            }
        }
        return featuresArray;
    }

    public int getDataSize() {
        return points.size();
    }

    public int getFeatureSize() {
        return nFeatures;
    }
}
