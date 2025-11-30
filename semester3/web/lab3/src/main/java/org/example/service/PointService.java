package org.example.service;

import org.example.models.PointCords;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
public class PointService implements Serializable {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

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

    public PointCords savePoint(double x, double y, double r) {
        boolean isHit = checkHit(x, y, r);
        PointCords point = new PointCords(x, y, r, isHit);
        em.persist(point);
        return point;
    }
}
