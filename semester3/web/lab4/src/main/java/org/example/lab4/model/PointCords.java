package org.example.lab4.model;

public class PointCords {
    private Long id;
    private String username;
    private double x;
    private double y;
    private double r;
    private boolean isHit;
    private Long timestamp;

    public PointCords() {
    }

    public PointCords(double x, double y, double r, String username, boolean isHit, Long timestamp) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.username = username;
        this.isHit = isHit;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = username;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
