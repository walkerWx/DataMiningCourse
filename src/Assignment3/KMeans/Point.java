package Assignment3.KMeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by walker on 15/10/28.
 */
public class Point {
    private ArrayList<Double> features;
    private int clusterNumber;

    public Point(ArrayList features){
        this.features = features;
    }

    public Point(ArrayList features, int clusterNumber) {
        this.features = features;
        this.clusterNumber = clusterNumber;
    }

    public void setCluster(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    public int getCluster() {
        return this.clusterNumber;
    }

    public List<Double> getFeatures() {
        return this.features;
    }

    public int getFeaturesNumber() {
        return features.size();
    }

    protected static double distance(Point p, Point centroid) {
        double sum = 0;
        for (int i =0; i != p.features.size(); ++i) {
            sum += Math.pow((p.features.get(i) - centroid.features.get(i)), 2);
        }
        return Math.sqrt(sum);
    }

    protected static Point createRandomPoint(double min, double max, int featureNumber) {
        Random r = new Random();
        ArrayList<Double> features = new ArrayList<>();
        for (int i = 0; i < featureNumber; ++i) {
            features.add(min + (max - min) * r.nextDouble());
        }
        return new Point(features);
    }

    @Override
    public String toString() {
        String s = "";
        s += "(";
        for (double feature : features) {
            s += feature;
            s += " , ";
        }
        s += ")";
        return s;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof Point)) return false;
        return this.features.equals(((Point)that).features);
    }
}
