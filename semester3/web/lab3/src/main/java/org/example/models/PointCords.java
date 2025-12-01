package org.example.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "points")
public class PointCords implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "points_id_generator")
    @SequenceGenerator(name = "points_id_generator", sequenceName = "points_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private double r;

    @Column(nullable = false)
    private boolean isHit;

    @Column(nullable = false)
    private Date timestamp;

    public PointCords() {
        this.x = 0;
        this.y = 0;
        this.r = 0;
        this.isHit = false;
        this.timestamp = new Date();
    }

    public PointCords(double x, double y, double r, boolean isHit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isHit = isHit;
        this.timestamp = new Date();
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
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", isHit=" + isHit +
                ", timestamp=" + timestamp +
                '}';
    }
}
