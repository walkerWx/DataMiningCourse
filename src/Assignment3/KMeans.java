package Assignment3;

import java.util.ArrayList;
import java.util.Arrays;
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

        InputReader ir = new InputReader(filePath);
        points = ir.getPoints();
        clusters = new ArrayList<>();

        // Create clusters and set random centroids
        List<Point> randomPoints = getRandomPoints(numOfClusters);
        for (int i = 0; i < numOfClusters; ++i) {
            Cluster cluster = new Cluster(i);
            cluster.setCentroid(randomPoints.get(i));
            clusters.add(cluster);
        }

    }

    public KMeans(double[][] data, int[] label, int numOfClusters) {
        points = new ArrayList<>();
        for (int i = 0; i < data.length; ++i) {
            Point p = new Point(i, Arrays.asList(data[i]), label[i]);
            points.add(p);
        }
        clusters = new ArrayList<>();

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

    public List<Point> getPoints() {
        return this.points;
    }

    private void assignCluster() {
        clearClusters();
        double max = Double.MAX_VALUE;
        double min = max;
        int clusterId = 0;
        double distance = 0.0;
        for (Point point : points) {
            min = max;
            for (int i = 0; i != clusters.size(); ++i) {
                Cluster c = clusters.get(i);
                distance = Point.distance(point, c.getCentroid());
                if (distance <= min) {
                    min = distance;
                    clusterId = i;
                }
            }
            point.setCluster(clusterId);
            clusters.get(clusterId).addPoint(point);
        }
    }

    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private void updateCentroids() {
        for (Cluster cluster : clusters) {
            ArrayList<Double> centroidFeatures = new ArrayList<>();
            int featuresNumber = points.get(0).getDimension();
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
            Point centroid = new Point(Point.RANDOM_POINT_ID, centroidFeatures);
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
        for (Point point : km.getPoints()) {
            System.out.println("ID:\t" + point.getId() + "\tClusterID:\t" + point.getCluster() + "\tLabel:\t" + point.getLabel());
        }
        ClusterValidator cv = new ClusterValidator(km.getPoints());
        System.out.print(cv.purity() + " " + cv.gini());
    }
}
