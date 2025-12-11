package org.example.lab4.DTO;

import java.util.Date;

public class PointResponse extends PointRequest {
    private boolean isHit;
    private Date timestamp;

    public PointResponse(double x, double y, double r, String username, boolean isHit) {
        super(x, y, r, username);
        this.isHit = isHit;
        this.timestamp = new Date();
    }

    public PointResponse(PointRequest pointRequest, boolean isHit) {
        super(pointRequest.getX(), pointRequest.getY(), pointRequest.getR(), pointRequest.getUsername());
        this.isHit = isHit;
        this.timestamp = new Date();
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date currentTime) {
        this.timestamp = currentTime;
    }
}
