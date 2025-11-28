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
    private final double x;

    @Column(nullable = false)
    private final double y;

    @Column(nullable = false)
    private final double r;

    @Column(nullable = false)
    private final boolean isHit;

    @Column(nullable = false)
    private final Date timestamp;

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
