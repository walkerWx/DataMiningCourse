package Assignment4;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/11/16.
 */
public class InputReader {
    private String file;
    private List<List<Integer>> data;
    private List<Integer> label;
    private int nFeatures;

    public InputReader(String file) {
        this.file = file;
        File fData = new File(file);
        data = new ArrayList<>();
        label = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fData), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] featuresString = line.split(",");
                nFeatures = featuresString.length - 1; // The last column represents the label of the corresponding example
                ArrayList<Integer> features = new ArrayList<>();
                for (int i = 0; i < nFeatures; ++i) {
                    features.add(Integer.parseInt(featuresString[i]));
                }
                data.add(features);
                label.add(Integer.parseInt(featuresString[featuresString.length - 1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<Integer>> getData() {
        return this.data;
    }

    public List<Integer> getLabel() {
        return this.label;
    }

    public int getScale() {
        return this.data.size();
    }

    public int getFeatureSize() {
        return this.nFeatures;
    }
}
