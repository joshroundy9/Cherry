package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Integer> {
    List<MealEntity> findByDateID(Integer dateID);
}