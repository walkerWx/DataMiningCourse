package Assignment3;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by walker on 15/11/7.
 */
public class ClusterValidator {

    private List<Point> points;
    List<Map<Integer, Integer>> confusionMatrix;
    private int nClusters;
    private int nLabels;

    public ClusterValidator(List<Point> points) {
        this.points = points;
        confusionMatrix = new ArrayList<>();
        Set<Integer> clusters = new HashSet<>();
        Set<Integer> labels = new HashSet<>();
        for (Point p : points) {
            clusters.add(p.getCluster());
            labels.add(p.getLabel());
        }

        nClusters = clusters.size();
        nLabels = labels.size();

        for (int i = 0; i < nClusters; ++i) {
            confusionMatrix.add(new HashMap<>());
        }

        for (Point p : points) {
            Map<Integer, Integer> m = confusionMatrix.get(p.getCluster());
            if (m.containsKey(p.getLabel())) {
                m.put(p.getLabel(), m.get(p.getLabel()) + 1);
            } else {
                m.put(p.getLabel(), 1);
            }
            confusionMatrix.set(p.getCluster(), m);
        }
    }

    double purity() {
        int sum = 0;
        for (Map<Integer, Integer> m : confusionMatrix) {
            sum += Collections.max(m.values());
        }
        return (double) sum / points.size();
    }

    public double gini() {
        List<Integer> M = new ArrayList<Integer>(Collections.nCopies(nClusters, 0));
        List<Double> G = new ArrayList<Double>(Collections.nCopies(nClusters, 0.0));

        for (int i = 0; i < nClusters; ++i) {
            M.set(i, confusionMatrix.get(i).values().stream().mapToInt(Integer::intValue).sum());
        }

        for (int i = 0; i < nClusters; ++i) {
            double mi = M.get(i);
            G.set(i, 1 - confusionMatrix.get(i).values().stream().map(v -> Math.pow(v, 2) / Math.pow(mi, 2)).mapToDouble(Double::doubleValue).sum());
        }

        double gini = IntStream.range(0, nClusters).mapToDouble(i -> G.get(i) * M.get(i)).sum() / M.stream().mapToInt(Integer::intValue).sum();

        return gini;
    }
}
