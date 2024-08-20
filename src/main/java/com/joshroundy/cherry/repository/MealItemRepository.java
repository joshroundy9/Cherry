package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.MealEntity;
import com.joshroundy.cherry.dataobject.entity.MealItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealItemRepository extends JpaRepository<MealItemEntity, Integer> {
    List<MealItemEntity> findByMealID(Integer mealID);
}
