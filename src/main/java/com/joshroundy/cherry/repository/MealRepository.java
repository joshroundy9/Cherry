package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Integer> {
    List<MealEntity> findByDateID(Integer dateID);
    void deleteByUserID(Integer dateID);

    @Modifying
    @Transactional
    @Query("UPDATE MealEntity m SET m.mealCalories = " +
            "(SELECT COALESCE(SUM(mi.itemCalories), 0) FROM MealItemEntity mi WHERE mi.mealID = :mealID), " +
            "m.mealProtein = " +
            "(SELECT COALESCE(SUM(mi.itemProtein), 0) FROM MealItemEntity mi WHERE mi.mealID = :mealID) " +
            "WHERE m.mealID = :mealID")
    void updateMealTotalsByMealID(@Param("mealID") Integer mealID);
}