package Assignment5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by walker on 15/12/1.
 */
public class DecisionTree {
    private Node root;

    private void buildTree(TrainingSet trainingSet, LabelSet labelSet, AttributeSet attributeSet) {
        root = buildTreeRec(trainingSet, labelSet, attributeSet);
    }

    private Node buildTreeRec(TrainingSet trainingSet, LabelSet labelSet, AttributeSet attributeSet) {

        if (trainingSet.isEmpty()) {
            return new LeafNode(new Label(0));
        }

        if (labelSet.allSame()) {
            return new LeafNode(labelSet.getLabels().get(0));
        }

        if (attributeSet.isEmpty()) {

            Map<Label, Integer> labelCount = new HashMap<>();
            for (Label label : labelSet.getLabels()) {
                if (labelCount.containsKey(label)) {
                    labelCount.put(label, labelCount.get(label) + 1);
                } else {
                    labelCount.put(label, 1);
                }
            }

            Label mostFrequentLabel = new Label(0);
            int maxCount = 0;
            for (Label label : labelCount.keySet()) {
                if (labelCount.get(label) > maxCount) {
                    mostFrequentLabel = label;
                    maxCount = labelCount.get(label);
                }
            }

            return new LeafNode(mostFrequentLabel);
        }

        return null;
    }

    private double info(TrainingSet trainingSet, LabelSet labelSet) {
        Map<Label, Integer> labelCount = new HashMap<>();
        for (Label label : labelSet.getLabels()) {
            if (labelCount.containsKey(label)) {
                labelCount.put(label, labelCount.get(label) + 1);
            } else {
                labelCount.put(label, 1);
            }
        }
        double info = 0.0;
        for (Label label : labelCount.keySet()) {
            double p = (double)labelCount.get(label) / labelSet.size();
            info -= p * Math.log(p);
        }
        return info;
    }

    private double info(TrainingSet trainingSet, LabelSet labelSet, Attribute attribute) {

        return 0.0;
    }


    private class Node {

    }

    private class LeafNode extends Node {

        private Label label;

        LeafNode(Label label) {
            this.label = label;
        }
    }

    private class BranchNode extends Node {
        private Attribute attr;
        private double threshold;
        private Map<Double, Node> sons;
    }
}
