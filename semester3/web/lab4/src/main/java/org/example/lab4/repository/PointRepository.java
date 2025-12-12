package org.example.lab4.repository;

import org.example.lab4.model.PointCords;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.tables.Points.POINTS;

@Repository
public class PointRepository {
    private final DSLContext dsl;

    public PointRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<PointCords> findAllByUsername(String username) {
        return dsl.selectFrom(POINTS)
                .where(POINTS.USERNAME.eq(username))
                .fetchInto(PointCords.class);
    }

    public void save(PointCords pointCords) {
        dsl.insertInto(POINTS)
                .set(POINTS.X, pointCords.getX())
                .set(POINTS.Y, pointCords.getY())
                .set(POINTS.R, pointCords.getR())
                .set(POINTS.IS_HIT, pointCords.isHit())
                .set(POINTS.USERNAME, pointCords.getUsername())
                .set(POINTS.TIMESTAMP, pointCords.getTimestamp())
                .execute();
    }

}
