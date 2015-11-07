package Assignment3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walker on 15/10/28.
 */
public class Cluster {
    private List<Point> points;
    private Point centroid;
    private int id;

    public Cluster(int id) {
        this.points = new ArrayList<>();
        this.centroid = null;
        this.id = id;
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List points) {
        this.points = points;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public int getId() {
        return this.id;
    }

    public void clear() {
        points = new ArrayList<>();
    }

}
