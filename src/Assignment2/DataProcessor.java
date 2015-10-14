package Assignment2;

import org.apache.commons.lang.UnhandledException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by walker on 15/10/11.
 */
public class DataProcessor {
    public static void main(String[] args) {

        // Minimum support value
        double minsup = 0.144;

        // Load data, you should set your own data file location here
        File data = new File("/Users/walker/Desktop/Courses/DataMining/Assignments/2/assignment2-data.txt");
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data), "utf-8"))) {
            String line;
            // Load item's name
            if ((line = br.readLine()) != null) {
                Collections.addAll(items, line.split("\\s+"));
            }

            // Load transactions
            Transaction transaction;
            while ((line = br.readLine()) != null) {
                transaction = new Transaction(line);
                transactions.add(transaction);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Collections.sort(transactions);
        transactions.forEach(t -> {
            t.items().forEach(item -> System.out.print(item + " "));
            System.out.println();
        });

        // apriori(transactions, minsup);
    }

    // The Apriori Algorithm
    private static List<Itemset> apriori(List<Transaction> transactions, double minsup) {

        if (transactions.isEmpty()) {
            return null;
        }

        // Generate frequent 1-itemset
        List<Integer> occurancesOfItems = new ArrayList<>();
        for (int i = 0; i != transactions.get(0).getNumOfItems(); ++i) {
            occurancesOfItems.add(0);
        }

        for (Transaction transaction : transactions) {
            for (int index : transaction.items()) {
                occurancesOfItems.set(index, occurancesOfItems.get(index) + 1);
            }
        }

        int numOfTransactions = transactions.size();
        List<Itemset> frequentOneItemsets = new ArrayList<>();
        for (int i = 0; i != occurancesOfItems.size(); ++i) {
            if ((double) occurancesOfItems.get(i) / numOfTransactions >= minsup) {
                Itemset itemset = new Itemset(i);
                frequentOneItemsets.add(itemset);
            }
        }

        // A list of itemsets to store the frequent k-itemsets, k = 1, 2, ...
        List<List<Itemset>> ListOfFrequentItemsets = new ArrayList<List<Itemset>>();
        ListOfFrequentItemsets.add(new ArrayList<Itemset>());   // case: k = 0, we fill it with an empty itemsets
        ListOfFrequentItemsets.add(frequentOneItemsets);        // case: k = 1

        List<Itemset> candidates;
        List<Itemset> frequentItemsets;
        int k = 1;
        while ((frequentItemsets = ListOfFrequentItemsets.get(k)) != null) {
            candidates = prune(generateCandidates(frequentItemsets), frequentItemsets);
            ListOfFrequentItemsets.add(determineFrequentItemsets(candidates, transactions, minsup));
            k++;
        }

        // Union the frequent itemsets as result
        List<Itemset> result = new ArrayList<>();
        ListOfFrequentItemsets.forEach(itemsets -> result.addAll(itemsets));
        return result;
    }

    // Generate C(k+1) by join itemset-pairs in F(k)
    private static List<Itemset> generateCandidates(List<Itemset> frequentItemsets) {

        if (frequentItemsets.isEmpty() || frequentItemsets.size() == 1) {
            return null;
        }

        Collections.sort(frequentItemsets);

        List<Itemset> candidates = new ArrayList<>();
        Itemset candidate;
        for (int i = 0, j = 1; i != frequentItemsets.size(); ) {
            while (j != frequentItemsets.size() && Itemset.generateCandidate(frequentItemsets.get(i), frequentItemsets.get(j)) != null) {
                ++j;
            }
            for (int k = i; k != j; ++k) {
                for (int l = k + 1; l != j; ++l) {
                    candidates.add(Itemset.generateCandidate(frequentItemsets.get(k), frequentItemsets.get(l)));
                }
            }
            i = j;
            j++;
        }

        return candidates;
    }

    // Prune itemsets from C(k+1) that violate downward closure
    private static List<Itemset> prune(List<Itemset> candidates, List<Itemset> frequentItemsets) {
        List<Itemset> prunedCandicates = new ArrayList<>();
        for (Itemset candidate : candidates) {
            if (frequentItemsets.containsAll(candidate.downwardClosure())) {
                prunedCandicates.add(candidate);
            }
        }
        return prunedCandicates;
    }

    // Determine F(k+1) by support counting on (C(K+1), T) and retaining itemsets from C(k+1) with support at least minsup
    private static List<Itemset> determineFrequentItemsets(List<Itemset> candicates, List<Transaction> transactions, double minsup) {
        return null;
    }

}
