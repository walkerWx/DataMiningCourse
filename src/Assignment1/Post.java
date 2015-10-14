package Assignment1;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/10/6.
 */
public class Post {

    private String content;
    private List<String> terms;

    public Post(String content) {
        this.content = content;
        terms = new ArrayList();
        try {
            IKSegmenter segmenter = new IKSegmenter(new StringReader(content), true);
            Lexeme lexeme;
            while ((lexeme = segmenter.next()) != null) {
                terms.add(lexeme.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public List<String> getTerms() {
        return  terms;
    }

}
