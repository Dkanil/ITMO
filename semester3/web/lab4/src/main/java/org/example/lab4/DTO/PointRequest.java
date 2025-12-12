package org.example.lab4.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PointRequest {
    @NotNull
    @Min(value = -5)
    @Max(value = 5)
    private double x;

    @NotNull
    @Min(value = -5)
    @Max(value = 5)
    private double y;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private double r;

    @NotNull
    private String username;

    public PointRequest(double x, double y, double r, String username) {
        this.x = x;
        this.y = y;
        this.r = r;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
