package Assignment5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by walker on 15/12/1.
 */
public class DecisionTree {
    private Node root;

    public DecisionTree(TrainingSet trainingSet, AttributeSet attributeSet) {
        buildTree(trainingSet, attributeSet);
    }

    private void buildTree(TrainingSet trainingSet, AttributeSet attributeSet) {
        root = buildTreeRec(trainingSet, attributeSet);
    }

    private Node buildTreeRec(TrainingSet trainingSet, AttributeSet attributeSet) {
        System.out.print("Buid tree rec called!" + " Training set size: " + trainingSet.size() + "\tAttributes: ");
        for (Attribute attribute : attributeSet.getAttributes()) {
            System.out.print(attribute.getAttributeIndex() + " ");
        }
        System.out.print("\n");

        if (trainingSet.isEmpty()) {
            return new LeafNode(new Label(0));
        }

        if (trainingSet.isAllSameLabel()) {
            return new LeafNode(trainingSet.getLabels().get(0));
        }

        if (attributeSet.isEmpty()) {
            System.out.println("May be error here!");
            Map<Label, Integer> labelCount = new HashMap<>();
            for (Label label : trainingSet.getLabels()) {
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

        Attribute maxGainAttr = new Attribute();
        double gain = 0.0, maxGain = 0.0;
        double threshold = 0.0;
        for (Attribute attribute : attributeSet.getAttributes()) {

            if (attribute.isDiscrete()) {
                gain = gain(trainingSet, attribute);
                if (gain >= maxGain) {
                    maxGain = gain;
                    maxGainAttr = attribute;
                }
            } else {
                List<Double> values = trainingSet.getAttributeValues(attribute);
                for (double value : values) {
                    gain = gain(trainingSet, attribute, value);
                    if (gain >= maxGain) {
                        maxGain = gain;
                        maxGainAttr = attribute;
                        threshold = value;
                    }
                }
            }
        }

        Map<Double, TrainingSet> partition = new HashMap<>();
        List<Double> item;
        Label label;
        double key;
        TrainingSet ts;
        if (maxGainAttr.isDiscrete()) {
            for (int i = 0; i < trainingSet.size(); ++i) {
                item = trainingSet.getItem(i);
                label = trainingSet.getLabel(i);
                key = item.get(maxGainAttr.getAttributeIndex());
                if (partition.containsKey(key)) {
                    ts = partition.get(key);
                } else {
                    ts = new TrainingSet();
                }
                ts.addItem(item, label);
                partition.put(key, ts);
            }

            Map<Double, Node> branches = new HashMap<>();
            for (double branch : partition.keySet()) {
                branches.put(branch, buildTreeRec(partition.get(branch), attributeSet.removeAttribute(maxGainAttr)));
            }
            return new BranchNode(branches, maxGainAttr);
        } else {
            partition.put(BranchNode.NO_MORE_THAN_KEY, new TrainingSet());
            partition.put(BranchNode.MORE_THAN_KEY, new TrainingSet());
            for (int i = 0; i < trainingSet.size(); ++i) {
                item = trainingSet.getItem(i);
                label = trainingSet.getLabel(i);
                key = item.get(maxGainAttr.getAttributeIndex());
                if (key <= threshold) {
                    ts = partition.get(BranchNode.NO_MORE_THAN_KEY);
                    ts.addItem(item, label);
                    partition.put(BranchNode.NO_MORE_THAN_KEY, ts);
                } else {
                    ts = partition.get(BranchNode.MORE_THAN_KEY);
                    ts.addItem(item, label);
                    partition.put(BranchNode.MORE_THAN_KEY, ts);
                }
            }
            Map<Double, Node> branches = new HashMap<>();
            branches.put(BranchNode.NO_MORE_THAN_KEY, buildTreeRec(partition.get(BranchNode.NO_MORE_THAN_KEY), attributeSet.removeAttribute(maxGainAttr)));
            branches.put(BranchNode.MORE_THAN_KEY, buildTreeRec(partition.get(BranchNode.MORE_THAN_KEY), attributeSet.removeAttribute(maxGainAttr)));
            return new BranchNode(branches, maxGainAttr);
 }     }

    public void print() {
        printNode(root);
    }

    public Label classify(List<Double> item) {
        return classify(item, root);
    }

    private Label classify(List<Double> item, Node node) {
        if (node instanceof LeafNode) {
            return ((LeafNode) node).label;
        } else {
            Attribute attribute = ((BranchNode) node).attribute;
            if (attribute.isDiscrete()) {
                return classify(item, ((BranchNode) node).branches.get(item.get(attribute.getAttributeIndex())));
            } else {
                if (item.get(attribute.getAttributeIndex()) <= ((BranchNode) node).threshold) {
                    return classify(item, ((BranchNode) node).branches.get(BranchNode.NO_MORE_THAN_KEY));
                } else {
                    return classify(item, ((BranchNode) node).branches.get(BranchNode.MORE_THAN_KEY));
                }
            }
        }
    }

    private void printNode(Node node) {
        if (node instanceof LeafNode) {
            System.out.println("Leaf node with label : " + ((LeafNode) node).label.getLabel());
        } else if (node instanceof BranchNode) {
            System.out.println("Branch node with attribute : " + ((BranchNode) node).attribute.getAttributeIndex());
            for (Node branch : ((BranchNode) node).branches.values()) {
                printNode(branch);
            }
        }
    }

    private double info(TrainingSet trainingSet) {
        Map<Label, Integer> labelCount = new HashMap<>();
        for (Label label : trainingSet.getLabels()) {
            if (labelCount.containsKey(label)) {
                labelCount.put(label, labelCount.get(label) + 1);
            } else {
                labelCount.put(label, 1);
            }
        }
        double info = 0.0;
        for (Label label : labelCount.keySet()) {
            double p = (double) labelCount.get(label) / trainingSet.size();
            info -= p * Math.log(p) / Math.log(2);
        }
        return info;
    }

    private double info(TrainingSet trainingSet, Attribute attribute) {
        double info = 0.0;
        assert attribute.isDiscrete();
        Map<Double, TrainingSet> partition = new HashMap<>();
        for (int i = 0; i != trainingSet.size(); ++i) {
            List<Double> item = trainingSet.getItem(i);
            Label label = trainingSet.getLabel(i);
            double key = item.get(attribute.getAttributeIndex());
            TrainingSet ts;
            if (partition.containsKey(key)) {
                ts = partition.get(key);
            } else {
                ts = new TrainingSet();
            }
            ts.addItem(item, label);
            partition.put(key, ts);
        }

        for (double key : partition.keySet()) {
            info += (double) partition.get(key).size() / trainingSet.size() * info(partition.get(key));
        }

        return info;
    }

    private double info(TrainingSet trainingSet, Attribute attribute, double threshold) {
        double info = 0.0;
        assert !attribute.isDiscrete();
        TrainingSet smaller = new TrainingSet();
        TrainingSet greater = new TrainingSet();
        for (int i = 0; i < trainingSet.size(); ++i) {
            List<Double> item = trainingSet.getItem(i);
            Label label = trainingSet.getLabel(i);
            double feature = item.get(attribute.getAttributeIndex());
            if (feature <= threshold) {
                smaller.addItem(item, label);
            } else {
                greater.addItem(item, label);
            }
        }
        info += (double) smaller.size() / trainingSet.size() * info(smaller);
        info += (double) greater.size() / trainingSet.size() * info(greater);
        return info;
    }

    private double gain(TrainingSet trainingSet, Attribute attribute) {
        assert attribute.isDiscrete();
        double gain = info(trainingSet) - info(trainingSet, attribute);
        return gain;
    }

    private double gain(TrainingSet trainingSet, Attribute attribute, double threshold) {
        assert !attribute.isDiscrete();
        return info(trainingSet) - info(trainingSet, attribute, threshold);
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
        public static final double NO_MORE_THAN_KEY = 0.0;
        public static final double MORE_THAN_KEY = 1.0;
        private Attribute attribute;
        private double threshold;
        private Map<Double, Node> branches;

        public BranchNode(Map<Double, Node> branches, Attribute attribute) {
            this.branches = branches;
            this.attribute = attribute;
        }

        public BranchNode(Map<Double, Node> branches, Attribute attribute, double threshold) {
            this.branches = branches;
            this.attribute = attribute;
            this.threshold = threshold;
        }
    }

    public static void main(String[] args) {
//        String path = "/Users/walker/Desktop/DataMining/german-assignment5.txt";
        String path = "/Users/walker/Desktop/DataMining/breast-cancer-assignment5.txt";
//        String path = "/Users/walker/Desktop/DataMining/mytest.txt";
        File fData = new File(path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fData), "utf-8"))) {
            String[] attributeTypes = br.readLine().split(",");
            AttributeSet attributeSet = new AttributeSet();
            for (int i = 0; i < attributeTypes.length; ++i) {
                if (attributeTypes[i].equals("1")) {
                    attributeSet.addAttribute(new Attribute(i, true));
                } else {
                    attributeSet.addAttribute(new Attribute(i, false));
                }
            }

            TrainingSet trainingSet = new TrainingSet();
            String line;
            List<Double> item;
            Label label;
            while ((line = br.readLine()) != null) {
                String[] lineValues = line.split(",");
                item = new ArrayList<>();
                for (int i = 0; i < lineValues.length - 1; ++i) {
                    item.add(Double.parseDouble(lineValues[i]));
                }
                label = new Label((int) Double.parseDouble(lineValues[lineValues.length - 1]));
                trainingSet.addItem(item, label);
            }

            DecisionTree decisionTree = new DecisionTree(trainingSet, attributeSet);
            int sameCount =0;
            for (int i = 0; i < trainingSet.size(); ++i) {
                if (trainingSet.getLabel(i).getLabel() == decisionTree.classify(trainingSet.getItem(i)).getLabel()) {
                    sameCount++;
                }
            }
            System.out.println((double)sameCount / trainingSet.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
