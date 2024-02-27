package com.boyapcky.bookworld.repository;

import com.boyapcky.bookworld.entity.MarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MarkRepository extends JpaRepository<MarkEntity, Long> {
    @Query(value = "SELECT m FROM MarkEntity m WHERE DATE(m.timeAdded) = :date")
    List<MarkEntity> findByTimeAdded(@Param(value = "date") Date date);
}
