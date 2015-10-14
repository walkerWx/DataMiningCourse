package Assignment1;

import java.io.*;
import java.util.*;

/**
 * Created by walker on 15/10/3.
 */
public class DataProcessor {
    public static void main(String[] args) {

        String directoryPath = "/Users/walker/Desktop/Courses/DataMining/Assignments/1/";

        // 载入Category与Post数据
        File dataFolder = new File(directoryPath + "lily");
        File[] dataFiles = dataFolder.listFiles();
        ArrayList<Category> categories = new ArrayList<Category>();
        for (File f : dataFiles) {
            if (f.isHidden()) {
                continue;
            }
            ArrayList<Post> posts = new ArrayList<Post>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    posts.add(new Post(line));
                }
                categories.add(new Category(f.getName().substring(0, f.getName().lastIndexOf('.')), posts));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Set<String> dictionary = new HashSet<>();
        Set<String> terms = new HashSet<>();
        Map postsWithTerm = new HashMap();
        int totalPosts = 0;
        for (Category ctg : categories) {
            for (Post post : ctg.getPosts()) {

                // 根据Post所包含的词组生成词典
                dictionary.addAll(post.getTerms());

                // 统计词典中每个次所出现的Post的数量
                terms.addAll(post.getTerms());
                for (String term : terms) {
                    if (!postsWithTerm.containsKey(term)) {
                        postsWithTerm.put(term, 1);
                    } else {
                        postsWithTerm.put(term, (int)postsWithTerm.get(term) +1);
                    }
                }
                terms.clear();

                // 统计Post总数
                totalPosts++;
            }
        }

        // 删除词典中的stop words
        dictionary.removeAll(StopWordsManager.getStopWords());



        PrintWriter writer;
        HashMap termFrequency = new HashMap();
        double tfidf = 0;
        try {
            // 判断输出文件夹是否存在，不存在则新建
            File output = new File(directoryPath + "result/");
            output.mkdir();

            for (Category ctg : categories) {
                writer = new PrintWriter(directoryPath + "result/" + ctg.getName(),"utf-8");
                for (Post post : ctg.getPosts()) {
                    termFrequency = new HashMap();
                    // 使用一个Hashmap存储一个Post中词语的词频信息
                    for (String term : post.getTerms()) {
                        if (!termFrequency.containsKey(term)) {
                            termFrequency.put(term, 1);
                        } else {
                            termFrequency.put(term, (Integer)termFrequency.get(term) + 1);
                        }
                    }
                    // 针对当前post,计算词典中每个词语的tfidf并输出到文件
                    for (String term : dictionary) {
                        if (post.getTerms().contains(term)) {
                            tfidf = TFIDF.calculate((int)termFrequency.get(term), totalPosts, (int)postsWithTerm.get(term));
                            writer.printf("%.4f\t", tfidf);
                        } else {
                            tfidf = 0;
                            writer.print("0\t");
                        }
                    }
                    writer.println();

                    // 清除当前Post的词频信息
                    termFrequency.clear();
                }
                writer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
