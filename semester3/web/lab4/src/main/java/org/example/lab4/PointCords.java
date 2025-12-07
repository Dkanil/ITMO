package org.example.lab4;

import java.io.Serializable;
import java.util.Date;

public class PointCords implements Serializable {
    private Long id;
    private String user;
    private double x;
    private double y;
    private double r;
    private boolean isHit;
    private Date timestamp;

    public PointCords() {
        this.id = 0L;
        this.user = "default";
        this.x = 0;
        this.y = 0;
        this.r = 0;
        this.isHit = false;
        this.timestamp = new Date();
    }

    public PointCords(String user, double x, double y, double r, boolean isHit) {
        this.user = user;
        this.x = x;
        this.y = y;
        this.r = r;
        this.isHit = isHit;
        this.timestamp = new Date();
    }

    public Long getId() {
        return id;
    }
    public String getUser() {
        return user;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getR() {
        return r;
    }
    public boolean isHit() {
        return isHit;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setR(double r) {
        this.r = r;
    }
    public void setHit(boolean hit) {
        isHit = hit;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PointCords{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", isHit=" + isHit +
                ", timestamp=" + timestamp +
                '}';
    }
}