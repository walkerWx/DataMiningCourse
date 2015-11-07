package Assignment3;

import Jama.Matrix;
import java.util.List;

/**
 * Created by walker on 15/10/30.
 */
public class NMF {

    private List<Point> points;
    private InputReader ir;
    private int nClusters;

    public NMF(String filePath, int nClusters) {
        ir = new InputReader(filePath);
        points = ir.getPoints();
        this.nClusters = nClusters;

    }

    public void calculate() {

        Matrix D = new Matrix(ir.get2dArray());

        // random initial matrix
        Matrix U = Matrix.random(ir.getDataSize(), nClusters);
        Matrix V = Matrix.random(ir.getFeatureSize(), nClusters);

        Matrix DV, UV_V, D_U, VU_U;
        boolean finish = false;

        // Iteration

        int iteration = 0;
        double lastJ = 1e10, currentJ = 1e9;
        double delta = lastJ - currentJ;
        while (delta > 1) {

            System.out.println("Iteration: " + iteration++);
            System.out.println(currentJ);

            // Avoid divide by zero
            double epsilon = 1e-9;

            // Update U and V
            DV = D.times(V); // D*V
            UV_V = U.times(V.transpose()).times(V); // U*V'*V
            UV_V.plusEquals(new Matrix(ir.getDataSize(), nClusters, epsilon));
            U = U.arrayTimes(DV).arrayRightDivide(UV_V);

            D_U = D.transpose().times(U); // D'*U
            VU_U = V.times(U.transpose()).times(U); // V*U'*U
            VU_U.plusEquals(new Matrix(ir.getFeatureSize(), nClusters, epsilon));
            V = V.arrayTimes(D_U).arrayRightDivide(VU_U);

            lastJ = currentJ;
            currentJ = cost(D.minus(U.times(V.transpose())).getArray());
            delta = lastJ - currentJ;
        }

        for (int i = 0; i < points.size(); ++i) {
            int clusterId = findMaxValueIndex(U.getArray()[i]);
            Point p = points.get(i);
            p.setCluster(clusterId);
            points.set(i, p);
        }

    }

    public List<Point> getPoints() {
        return this.points;
    }

    private int findMaxValueIndex(double[] array) {
        int index = 0;
        double maxValue = array[0];
        for (int i = 0; i < array.length; ++i) {
            if (array[i] > maxValue) {
                index = i;
                maxValue = array[i];
            }
        }
        return index;
    }

    private double cost(double[][] matrix) {
        double cost = 0;
        for (double[] row : matrix) {
            for (double e : row) {
                cost += Math.pow(e, 2);
            }
        }
        return cost;
    }


    public static void main(String[] args) {
        int K = 2; // number of clusters
//        String filePath = "/Users/walker/Desktop/DataMining/german.txt"; // data file path
        String filePath = "/Users/walker/Desktop/DataMining/mnist.txt"; // data file path


        NMF nmf = new NMF(filePath, K);
        nmf.calculate();
        ClusterValidator cv = new ClusterValidator(nmf.getPoints());
        System.out.print(cv.purity() + " " + cv.gini());

    }
}
