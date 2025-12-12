package org.example.lab4.DTO;

public class PointResponse extends PointRequest {
    private boolean isHit;
    private Long timestamp;

    public PointResponse(double x, double y, double r, String username, boolean isHit, Long timestamp) {
        super(x, y, r, username);
        this.isHit = isHit;
        this.timestamp = timestamp;
    }

    public PointResponse(PointRequest pointRequest, boolean isHit, Long timestamp) {
        super(pointRequest.getX(), pointRequest.getY(), pointRequest.getR(), pointRequest.getUsername());
        this.isHit = isHit;
        this.timestamp = timestamp;
    }

    public boolean getIsHit() {
        return isHit;
    }

    public void setIsHit(boolean hit) {
        isHit = hit;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long currentTime) {
        this.timestamp = currentTime;
    }
}
