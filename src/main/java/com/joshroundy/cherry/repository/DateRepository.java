package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DateRepository extends JpaRepository<DateEntity, Integer> {
    List<DateEntity> findByUserID(Integer userID);
    Optional<DateEntity> findByUserIDAndDate(Integer userID, Date date);
    List<DateEntity> findByUserIDAndDateBetween(Integer userID, Date startDate, Date endDate);
    void deleteByUserID(Integer userID);

    @Modifying
    @Transactional
    @Query("UPDATE DateEntity d SET d.dailyCalories = " +
            "(SELECT COALESCE(SUM(m.mealCalories), 0) FROM MealEntity m WHERE m.dateID = :dateID), " +
            "d.dailyProtein = " +
            "(SELECT COALESCE(SUM(m.mealProtein), 0) FROM MealEntity m WHERE m.dateID = :dateID) " +
            "WHERE d.dateID = :dateID")
    void updateDailyTotalsByDateID(@Param("dateID") Integer dateID);
}