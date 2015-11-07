package Assignment3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by walker on 15/10/28.
 */
public class Point {

    public static final int RANDOM_POINT_ID = -1;

    private int id;
    private int clusterId;
    private int label;
    private List<Double> features;

    public Point(int id, List features) {
        this.id = id;
        this.features = features;
    }

    public Point(int id, List features, int label) {
        this.id = id;
        this.features = features;
        this.label = label;
        this.clusterId = -1;
    }

    public int getId() {
        return this.id;
    }

    public void setCluster(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getCluster() {
        return this.clusterId;
    }

    public int getLabel() {
        return this.label;
    }

    public List<Double> getFeatures() {
        return this.features;
    }

    public int getDimension() {
        return features.size();
    }

    protected static double distance(Point a, Point b) {
        double sum = 0;
        for (int i = 0; i != a.features.size(); ++i) {
            sum += Math.pow((a.features.get(i) - b.features.get(i)), 2);
        }
        return sum;
//        return Math.sqrt(sum);
    }

    protected static Point createRandomPoint(double min, double max, int featureNumber) {
        Random r = new Random();
        ArrayList<Double> features = new ArrayList<>();
        for (int i = 0; i < featureNumber; ++i) {
            features.add(min + (max - min) * r.nextDouble());
        }
        return new Point(RANDOM_POINT_ID, features);
    }

    @Override
    public String toString() {
        String s = "";
        s += "(";
        for (double feature : features) {
            s += "\t";
            s += feature;
            s += "\t";
        }
        s += ")";
        return s;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof Point)) return false;
        return this.features.equals(((Point) that).features);
    }
}
