package Assignment5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by walker on 15/12/5.
 */
public class RandomForest implements Classifier {
    private static final int FOREST_SIZE = 100;
    private List<DecisionTree> forest;

    public RandomForest(TrainingSet trainingSet, AttributeSet attributeSet) {
        forest = new ArrayList<>();
        // build decision trees
        for (int i = 0; i < FOREST_SIZE; ++i) {
            forest.add(new DecisionTree(trainingSet.randomSample(), attributeSet.randomSample()));
//            forest.add(new DecisionTree(trainingSet.randomSample(), attributeSet));
        }
    }

    public Label classify(List<Double> item) {
        Map<Label, Integer> votes = new HashMap<>();
        for (DecisionTree decisionTree : forest) {
            Label label = decisionTree.classify(item);
            if (votes.containsKey(label)) {
                votes.put(label, votes.get(label) + 1);
            } else {
                votes.put(label, 1);
            }
        }
        Label maxVotesLabel = new Label();
        int maxVotesCount = 0;
        for (Label label : votes.keySet()) {
            if (votes.get(label) >= maxVotesCount) {
                maxVotesCount = votes.get(label);
                maxVotesLabel = label;
            }
        }
        return maxVotesLabel;
    }
}
