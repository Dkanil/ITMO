package org.example.lab4.service;

import org.example.lab4.DTO.PointRequest;
import org.example.lab4.DTO.PointResponse;
import org.example.lab4.entity.PointCords;
import org.example.lab4.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    private boolean checkHit(double x, double y, double r) {
        if (x >= 0 && y >= 0) {
            return (y <= r / 2) && (x <= r); // 1 четверть
        } else if (x <= 0 && y <= 0) {
            return (x * x + y * y) <= (r * r); // 3 четверть
        } else if (x <= 0 && y >= 0) {
            return y <= x + r / 2; // 2 четверть
        }
        return false; // 4 четверть
    }

    public PointResponse processAndSavePoint(PointRequest point, String username) {
        boolean isHit = checkHit(point.getX(), point.getY(), point.getR());
        PointCords pointCords = new PointCords(
                point.getX(),
                point.getY(),
                point.getR(),
                username,
                isHit,
                System.currentTimeMillis());

        pointRepository.save(pointCords);
        return new PointResponse(point, isHit);
    }

    public List<PointCords> findAllByUsername(String username) {
        return pointRepository.findAllByUsername(username);
    }
}
