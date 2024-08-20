package com.joshroundy.cherry.repository;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Integer> {
    //Optional<List<MealEntity>> findByDateID(Integer DateID);
}