package org.example.lab4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.lab4.entity.PointCords;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<PointCords, Long> {
    List<PointCords> findAllByUsername(String username);
}
