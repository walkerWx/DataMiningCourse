package Assignment3.KMeans;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;

/**
 * Created by walker on 15/10/28.
 */
public class KMeans {
    private static final Random random = new Random();
    private List<Point> points;
    private List<Cluster> clusters;

    public KMeans(String filePath, int numOfClusters) {
        points = new ArrayList<>();
        clusters = new ArrayList<>();

        // Load feature data
        File data = new File(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] featuresString = line.split(",");
                int featuresNumber = featuresString.length - 1; // The last column represents the label of the corresponding example
                ArrayList<Double> features = new ArrayList<>();
                for (int i = 0; i < featuresNumber; ++i) {
                    features.add(Double.parseDouble(featuresString[i]));
                }
                points.add(new Point(features));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create clusters and set random centroids
        List<Point> randomPoints = getRandomPoints(numOfClusters);
        for (int i = 0; i < numOfClusters; ++i) {
            Cluster cluster = new Cluster(i);
            cluster.setCentroid(randomPoints.get(i));
            clusters.add(cluster);
        }
    }

    public void calculate() {
        boolean finish = false;
        int iteration = 0;

        while (!finish) {
            List<Point> lastCentroids = getCentroids();
            assignCluster();
            updateCentroids();
            ++iteration;
            List<Point> currentCentroids = getCentroids();
            double distance = 0.0;
            for (int i = 0; i != lastCentroids.size(); ++i) {
                distance += Point.distance(lastCentroids.get(i), currentCentroids.get(i));
            }
            System.out.println("#########################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distance: " + distance);

            if (distance == 0) {
                finish = true;
            }
        }
    }

    private void assignCluster() {
        clearClusters();
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;
        for (Point point : points) {
            min = max;
            for (int i = 0; i != clusters.size(); ++i) {
                Cluster c = clusters.get(i);
                distance = Point.distance(point, c.getCentroid());
                if (distance <= min) {
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }

    private void clearClusters() {
        for(Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private void updateCentroids() {
        for (Cluster cluster : clusters) {
            ArrayList<Double> centroidFeatures = new ArrayList<>();
            int featuresNumber = points.get(0).getFeaturesNumber();
            for (int i = 0; i != featuresNumber; ++i) {
                centroidFeatures.add(.0);
            }
            for (Point point : cluster.getPoints()) {
                List<Double> features = point.getFeatures();
                for (int i = 0; i != featuresNumber; ++i) {
                    centroidFeatures.set(i, centroidFeatures.get(i) + features.get(i));
                }
            }
            for (int i = 0; i != featuresNumber; ++i) {
                centroidFeatures.set(i, centroidFeatures.get(i) / cluster.getPoints().size());
            }
            Point centroid = new Point(centroidFeatures);
            cluster.setCentroid(centroid);
        }
    }

    private List<Point> getCentroids() {
        List<Point> centroids = new ArrayList<>();
        for (Cluster c : clusters) {
            centroids.add(c.getCentroid());
        }
        return centroids;
    }

    private List<Point> getRandomPoints(int k) {
        if (points.size() < k) {
            return null;
        }

        List<Point> randomPoints = new ArrayList<>();
        Point point;
        while (true) {
            point = points.get(random.nextInt(points.size()));
            if (!randomPoints.contains(point)) {
                randomPoints.add(point);
                --k;
            }
            if (k == 0) {
                break;
            }
        }
        return randomPoints;
    }

    public static void main(String[] args) {
//        String filePath = "/Users/walker/Desktop/DataMining/german.txt";
        String filePath = "/Users/walker/Desktop/DataMining/mnist.txt";
        KMeans km = new KMeans(filePath, 10);
        km.calculate();
    }
}
