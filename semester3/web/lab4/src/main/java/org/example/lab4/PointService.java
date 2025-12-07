package org.example.lab4;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {

    List<PointCords> points = new ArrayList<>();

    private boolean checkHit(double x, double y, double r) {
        if (x >= 0 && y >= 0) {
            return y <= - x + r / 2; // 1 четверть
        } else if (x <= 0 && y >= 0) {
            return (x * x + y * y) <= (r * r); // 2 четверть
        } else if (x <= 0 && y <= 0) {
            return (y >= - r) && (x >= - r / 2); // 3 четверть
        }
        return false; // 4 четверть
    }

    public PointCords savePoint(String user, double x, double y, double r) {
        boolean isHit = checkHit(x, y, r);
        PointCords point = new PointCords(user, x, y, r, isHit);
        points.add(point);
        return point;
    }

    public List<PointCords> getAllPoints() {
        return points;
    }
}
