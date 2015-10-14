package Assignment1;

import java.util.ArrayList;

/**
 * Created by walker on 15/10/6.
 */
public class Category {
    private String name;
    private ArrayList<Post> posts;

    public Category(String name, ArrayList<Post> posts) {
        this.name = name;
        this.posts = posts;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
