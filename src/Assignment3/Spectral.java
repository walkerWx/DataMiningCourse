package Assignment3;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by walker on 15/11/6.
 */
public class Spectral {
    public static void main(String[] args) {
        int K = 2; // number of clusters
        int N = 3; // nearest N points
        List<Point> points = new ArrayList<>(); // point list
        String filePath = "/Users/walker/Desktop/DataMining/german.txt"; // data file path
//        String filePath = "/Users/walker/Desktop/DataMining/mnist.txt"; // data file path
        File fData = new File(filePath);
        List<Integer> labels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fData), "utf-8"))) {
            String line;
            int id = 0;
            while ((line = br.readLine()) != null) {
                String[] sCoordinateValues = line.split(",");
                int dimension = sCoordinateValues.length - 1; // The last column represents the label of the corresponding example
                List<Double> coordinateValues = new ArrayList<>();
                for (int i = 0; i < dimension; ++i) {
                    coordinateValues.add(Double.parseDouble(sCoordinateValues[i]));
                }
                labels.add(Integer.parseInt(sCoordinateValues[sCoordinateValues.length - 1]));
                points.add(new Point(id, coordinateValues));
                ++id;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // a list to store all nearest points of every point
        List<List<Point>> nearestPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); ++i) {
            nearestPoints.add(findNearestPoints(points.get(i), points, N));
        }

        // construct the adjacent matrix W
        int[][] W = new int[points.size()][points.size()];
        for (int i = 0; i < points.size(); ++i) {
            for (int j = 0; j < points.size(); ++j) {
                W[i][j] = 0;
            }
        }
        for (int i = 0; i < nearestPoints.size(); ++i) {
            for (Point p : nearestPoints.get(i)) {
                W[i][p.getId()] = 1;
                W[p.getId()][i] = 1;
            }
        }

        // construct D inverse
        double[][] DI = new double[points.size()][points.size()];
        for (int i = 0; i < points.size(); ++i) {
            for (int j = 0; j < points.size(); ++j) {
                DI[i][j] = 0;
            }
        }
        for (int i = 0; i < points.size(); ++i) {
            for (int j = 0; j < points.size(); ++j) {
                DI[i][i] += W[j][i];
            }
            DI[i][i] = 1.0 / DI[i][i];
        }


        // construct L
        double[][] L = new double[points.size()][points.size()];
        for (int i = 0; i < points.size(); ++i) {
            for (int j = 0; j < points.size(); ++j) {
                L[i][j] = DI[i][j] - W[i][j];
            }
        }

        Matrix mDI = new Matrix(DI);
        Matrix mL = new Matrix(L);
        EigenvalueDecomposition ed = new EigenvalueDecomposition(mDI.times(mL));
        Matrix eigenVector = ed.getV();
        eigenVector.print(1, 5);

        int[] labelArray = new int[labels.size()];
        for (int i = 0; i < labelArray.length; ++i) {
            labelArray[i] = labels.get(i);
        }
        KMeans km = new KMeans(eigenVector.getArray(), labelArray, K);
        km.calculate();
        ClusterValidator cv = new ClusterValidator(km.getPoints());
        System.out.print(cv.purity() + " " + cv.gini());

    }

    // find the nearest n points of a point
    static List<Point> findNearestPoints(Point point, List<Point> points, int n) {
        PriorityQueue<Point> queue = new PriorityQueue<>(n, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double d1 = Point.distance(point, o1);
                double d2 = Point.distance(point, o2);
                if (d1 > d2) {
                    return -1;
                } else if (d1 < d2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        for (Point p : points) {
            if (p != point) {
                queue.add(p);
            }
            if (queue.size() > n) {
                queue.poll();
            }
        }

//        System.out.print(point.getId() + "\t:\t");
//        queue.forEach(p->System.out.print(p.getId()+"\t"));
//        System.out.println("");

        return new ArrayList<>(queue);
    }
}