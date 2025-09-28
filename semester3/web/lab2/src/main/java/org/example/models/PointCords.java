package org.example.models;

import org.example.CordsValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Date;

public class PointCords implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private final double x;
    private final double y;
    private final double r;
    private final boolean isHit;
    private final Date timestamp;
    private double executionTime = 0.0;

    public PointCords(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query string cannot be null or empty");
        }
        CordsValidator cordsValidator = new CordsValidator();
        Map<String, String> params = cordsValidator.splitQuery(query);
        try {
            this.x = Double.parseDouble(params.get("x"));
            this.y = Double.parseDouble(params.get("y"));
            this.r = Double.parseDouble(params.get("r"));
            if (!cordsValidator.validateParams(x, y, r)) {
                System.err.println("Validation failed for parameters: x=" + x + ", y=" + y + ", r=" + r);
                throw new IllegalArgumentException("Parameter values are out of valid range");
            }
            this.isHit = checkHit();
            this.timestamp = new Date();
        }
        catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid parameter format");
        }
    }

    private boolean checkHit() {
        if (x >= 0 && y >= 0) {
            return y <= - x + r; // 1 четверть
        } else if (x <= 0 && y >= 0) {
            return (x * x + y * y) <= (r * r / 4); // 2 четверть
        } else if (x >= 0 && y <= 0) {
            return (y >= - r / 2) && (x <= r); // 4 четверть
        }
        return false; // 3 четверть
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

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "PointCords{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", isHit=" + isHit +
                ", timestamp=" + timestamp +
                ", executionTime=" + executionTime +
                '}';
    }
}
