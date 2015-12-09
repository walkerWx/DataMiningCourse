package Assignment5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by walker on 15/12/6.
 */
public class AdaBoost implements Classifier {

    private List<DecisionTree> classifiers;
    private List<Double> alphaList;

    public AdaBoost(TrainingSet trainingSet, AttributeSet attributeSet, int rounds) {
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < trainingSet.size(); ++i) {
            weights.add(1.0 / trainingSet.size());
        }
        DecisionTree decisionTree ;
//        int sameCount = 0;
//        for (int j = 0; j < trainingSet.size(); ++j) {
//            assert trainingSet.getItem(j) != null && trainingSet.getLabel(j) != null;
//            if (trainingSet.getLabel(j) == decisionTree.classify(trainingSet.getItem(j))) {
//                sameCount++;
//            }
//        }
//        System.out.print((double) sameCount / trainingSet.size());

        classifiers = new ArrayList<>();
        alphaList = new ArrayList<>();
        double weightedErrorRate = 0.0;
        for (int t = 0; t < rounds; ++t) {
            decisionTree = new DecisionTree(trainingSet.weightedSample(weights), attributeSet);
            classifiers.add(decisionTree);

            // calculate the weighted error rate
            weightedErrorRate = 0.0;
            for (int i = 0; i < trainingSet.size(); ++i) {
                if (decisionTree.classify(trainingSet.getItem(i)).getLabel() != trainingSet.getLabel(i).getLabel()) {
                    weightedErrorRate += weights.get(i);
                }
            }

            double alpha = 0.5 * Math.log((1 - weightedErrorRate) / weightedErrorRate);
            alphaList.add(alpha);

            // update weights
            for (int i = 0; i < trainingSet.size(); ++i) {
                if (decisionTree.classify(trainingSet.getItem(i)) != trainingSet.getLabel(i)) {
                    weights.set(i, weights.get(i) * Math.exp(alpha));
                } else {
                    weights.set(i, weights.get(i) * Math.exp(-alpha));
                }
            }

            // normalization
            double sum = 0.0;
            for (int i = 0; i < trainingSet.size(); ++i) {
                sum += weights.get(i);
            }
            for (int i = 0; i < trainingSet.size(); ++i) {
                weights.set(i, weights.get(i) / sum);
            }

            double ws = 0.0;
            System.out.println("weighted error rate: " + weightedErrorRate);
            System.out.println("alpha: " + alpha);
            for (double weight : weights) {
                System.out.format(" %3f", weight);
                ws += weight;
            }
            System.out.print("\nSum of weights: " + ws + "\n");
            if (weightedErrorRate == 0 || weightedErrorRate >= 0.5) {
                break;
            }
        }
    }

    public Label classify(List<Double> item) {
        Label label = null;
        Map<Label, Double> votes = new HashMap<>();
        for (int i = 0; i < classifiers.size(); ++i) {
            label = classifiers.get(i).classify(item);
            if (votes.containsKey(label)) {
                votes.put(label, votes.get(label) + alphaList.get(i));
            } else {
                votes.put(label, alphaList.get(i));
            }
        }
        Label maxVotesLabel = null;
        double maxVotes = 0.0;
        for (Label candidate : votes.keySet()) {
            if (votes.get(candidate) >= maxVotes) {
                maxVotesLabel = candidate;
                maxVotes = votes.get(candidate);
            }
        }
        return maxVotesLabel;
    }
}
