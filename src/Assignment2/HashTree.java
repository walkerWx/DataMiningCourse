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


        this.threshold = Math.max((int) Math.sqrt(numOfItems),2);
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

    public Set<Itemset> candidatesInTransaction(Transaction transaction) {


        List<Itemset> candidatesList = dfs(0, transaction, root);
        if (candidatesList == null) {
            return null;
        }
        TreeSet<Itemset> candidates = new TreeSet<>(candidatesList);
        return candidates;
    }


    private List<Itemset> dfs(int index, Transaction transaction, TreeNode treeNode) {

        if (treeNode == null) {
            return null;
        }

        if (treeNode.isLeaf) {
            return treeNode.candidates;
        }

        if (index >= transaction.getNumOfContentItems()) {
            return null;
        }

        if (treeNode.isLeaf) {
            return treeNode.candidates;
        } else {
            List<Itemset> result = new ArrayList<>();
            TreeNode nextNode;
            for (int i = index; i != transaction.getNumOfContentItems(); ++i) {
                nextNode = treeNode.branches.get(hash(transaction, i));
                List<Itemset> temp = dfs(i + 1, transaction, nextNode);
                if (temp != null) {
                    result.addAll(temp);
                }
            }
            return result;
        }
    }


    private void addCandidate(Itemset candidate) {
        root.insert(candidate);
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


    }

    int hash(Itemset itemset, int index) {
        return itemset.getItem(index) % threshold;
    }

    int hash(Transaction transaction, int index) {
        return transaction.getItem(index) % threshold;
    }

}
