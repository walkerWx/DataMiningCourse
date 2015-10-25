package Assignment2;

import apple.laf.JRSUIUtils;
import com.intellij.vcs.log.Hash;
import org.apache.sanselan.common.ImageMetadata;

import java.io.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by walker on 15/10/16.
 */
public class HashTree {

    private TreeNode root;
    private int threshold;

    public HashTree(List<Itemset> candidates, int numOfItems) {

        this.threshold = (int) Math.sqrt(numOfItems);
        root = new TreeNode(threshold);

        // Build the hash tree
        for (int i = 0; i != candidates.size(); ++i) {
            addCandidate(candidates.get(i));
        }
    }

    HashTree(TreeNode root) {
        this.root = root;
        this.threshold = root.threshold;
    }

    public List<Itemset> candidatesInTransaction(Transaction transaction) {
        return null;
    }

    public void print() {
        Stack<TreeNode> s = new Stack<>();
        TreeNode tn;
        HashTree hs;
        s.push(root);
        while (!s.empty()) {
            tn = s.pop();
            if (tn.isLeaf) {
                System.out.println("A leaf node with depth:" + tn.depth + "---------------------------");
                System.out.println("*");
                for (Itemset itemset : tn.candidates) {
                    System.out.println(itemset);
                }
                System.out.println("*");
                System.out.println("end -----------------------------------\n");
            } else {
                for (TreeNode branch : tn.branches.values()) {
                    s.push(branch);
                }
            }
        }

    }

    private void addCandidate(Itemset candidate) {
        root.insert(candidate);
    }

    // Build the hash tree from the candidates
    private void buildHashTree() {

    }

    private class TreeNode {
        private int depth;
        private int threshold;
        private boolean isLeaf;
        private List<Itemset> candidates;
        private Hashtable<Integer, TreeNode> branches;

        TreeNode(int threshold) {
            this.depth = 0;
            this.threshold = threshold;
            this.isLeaf = false;
            this.candidates = null;
            this.branches = new Hashtable<>();
        }

        TreeNode(int threshold, int depth, boolean isLeaf) {
            this.threshold = threshold;
            this.depth = depth;
            this.isLeaf = isLeaf;
            if (isLeaf) {
                candidates = new ArrayList<>();
                branches = null;
            } else {
                candidates = null;
                branches = new Hashtable<>();
            }
        }

        void insert(Itemset itemset) {
            if (isLeaf) {
                if (candidates.size() >= threshold) {
                    candidates.add(itemset);
                    if (depth == itemset.getNumOfItems()) {
                        return;
                    } else {
                        branches = new Hashtable<>();
                        for (int i = 0; i != candidates.size(); ++i) {
                            TreeNode tn = branches.get(hash(candidates.get(i), depth));
                            if (tn != null) {
                                tn.insert(candidates.get(i));
                            } else {
                                tn = new TreeNode(threshold, depth + 1, true);
                                tn.insert(candidates.get(i));
                                branches.put(hash(candidates.get(i), depth), tn);
                            }
                        }
                        isLeaf = false;
                        candidates = null;
                    }
                } else {
                    candidates.add(itemset);
                }

            } else {
                TreeNode tn = branches.get(hash(itemset, depth));
                if (tn == null) {
                    tn = new TreeNode(threshold, depth + 1, true);
                }
                tn.insert(itemset);
                branches.put(hash(itemset, depth), tn);
            }
        }


        int hash(Itemset itemset, int index) {
            return itemset.getItem(index) % threshold;
        }
    }

    public static void main(String[] args) {
        // Load data, you should set your own data file location here
        File data = new File("/Users/walker/Desktop/data");
        ArrayList<Itemset> candidates = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "utf-8"))) {
            String line;
            // Load transactions
            Itemset itemset;
            while ((line = br.readLine()) != null) {
                itemset = new Itemset(line);
                candidates.add(itemset);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(candidates);
        for (Itemset itemset : candidates)  {
            System.out.println(itemset);
        }

        HashTree ht = new HashTree(candidates, candidates.get(0).getNumOfItems());
        ht.print();
    }

}
