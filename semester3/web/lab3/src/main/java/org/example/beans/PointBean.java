package org.example.beans;

import org.example.models.PointCords;
import org.example.service.PointService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named("pointBean")
@SessionScoped
public class PointBean implements Serializable {

    private double x;
    private double y;
    private double r = 1;
    private final List<Double> xValues = Arrays.asList(-5d, -4d, -3d, -2d, -1d, 0d, 1d, 2d, 3d);

    private ArrayList<PointCords> results = new ArrayList<>();

    @Inject
    private PointService pointService;

    public void submit() {
        PointCords p = pointService.savePoint(x, y, r);
        results.add(p);
    }

    public void loadResults() {
        results = pointService.getAllPoints();
    }

    public List<Double> getxValues() { return xValues; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getR() { return r; }
    public void setR(double r) { this.r = r; }

    public ArrayList<PointCords> getResults() { return results; }

}
