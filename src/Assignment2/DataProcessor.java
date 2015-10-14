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
    }

    // The Apriori Algorithm
    private List<Itemset> Apriori(List<Transaction> transactions, double minsup) {

        return null;
    }

    // Generate C(k+1) by join itemset-pairs in F(k)
    private ArrayList<Itemset> generateCandidates(ArrayList<Itemset> frequentItemsets) {
        if (frequentItemsets.isEmpty() || frequentItemsets.size() == 1) {
            return null;
        }
        return null;
    }

    // Prune itemsets from C(k+1) that violate downward closure
    private List<Itemset> prune(List<Itemset> candidates, List<Itemset> frequentItemsets) {
        return null;
    }

    // Determine F(k+1) by support counting on (C(K+1), T) and retaining itemsets from C(k+1) with support at least minsup
    private List<Itemset> determineFrequentItemsets(List<Itemset> candicates, List<Transaction> transactions, double minsup) {
        return null;
    }

}
