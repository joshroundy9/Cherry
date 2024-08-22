package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.data.DateDTO;
import com.joshroundy.cherry.dataobject.data.MealDTO;
import com.joshroundy.cherry.dataobject.data.MealItemDTO;
import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.entity.MealEntity;
import com.joshroundy.cherry.dataobject.entity.MealItemEntity;
import com.joshroundy.cherry.repository.DateRepository;
import com.joshroundy.cherry.repository.MealItemRepository;
import com.joshroundy.cherry.repository.MealRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DataService {
    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final DateRepository dateRepository;
    /*Date methods*/
    public List<DateEntity> findDatesFromUserID(Integer userID) {
        return dateRepository.findByUserID(userID);
    }
    public DateEntity createDate(DateDTO dateDTO) {
        return dateRepository.save(
                DateEntity.builder()
                        .date(dateDTO.getDate())
                        .userID(dateDTO.getUserID())
                        .dailyWeight(dateDTO.getDailyWeight())
                        .build()
        );
    }
    public DateEntity updateDateWeight(Integer dateID, Double weight) {
        var dateEntity = dateRepository.findById(dateID).get();
        dateEntity.setDailyWeight(weight);
        return dateRepository.save(dateEntity);
    }
    public DateEntity findDateByUserIDAndDate(Integer userID, Date date) {
        return findDatesFromUserID(userID).stream().filter(
                dateEntity -> date.equals(dateEntity.getDate())
        ).findFirst().orElse(null);
    }
    /*Meal methods*/
    public List<MealEntity> findMealsFromDateID(Integer dateID) {
        return mealRepository.findByDateID(dateID);
    }
    public MealEntity createMeal(MealDTO mealDTO) {
        return mealRepository.save(MealEntity.builder()
                        .userID(mealDTO.getUserID())
                        .dateID(mealDTO.getDateID())
                        .dateTime(mealDTO.getDateTime())
                .build());
    }
    public MealEntity updateMealTime(Integer mealID, ZonedDateTime dateTime) {
        var mealEntity = mealRepository.findById(mealID).get();
        mealEntity.setDateTime(dateTime);
        return mealRepository.save(mealEntity);
    }
    public void deleteMeal(Integer mealID) {
        mealItemRepository.deleteAll(findMealItemsFromMealID(mealID));
        mealRepository.deleteById(mealID);
    }
    /*Meal item methods*/
    public List<MealItemEntity> findMealItemsFromMealID(Integer mealID) {
        return mealItemRepository.findByMealID(mealID);
    }
    public MealItemEntity createMealItem(MealItemDTO mealItemDTO) {
        return mealItemRepository.save(MealItemEntity.builder()
                        .userID(mealItemDTO.getUserID())
                        .itemCalories(mealItemDTO.getItemCalories())
                        .itemName(mealItemDTO.getItemName())
                        .dateID(mealItemDTO.getDateID())
                        .mealID(mealItemDTO.getMealID())
                .build());
    }
    public MealItemEntity updateMealItemName(Integer mealItemID, String mealItemName) {
        var mealItemEntity = mealItemRepository.findById(mealItemID).get();
        mealItemEntity.setItemName(mealItemName);
        return mealItemRepository.save(mealItemEntity);
    }
    public MealItemEntity updateMealItemCalories(Integer mealItemID, Integer mealItemCalories) {
        var mealItemEntity = mealItemRepository.findById(mealItemID).get();
        mealItemEntity.setItemCalories(mealItemCalories);
        return mealItemRepository.save(mealItemEntity);
    }
    public void deleteMealItem(Integer mealItemID) {
        mealItemRepository.deleteById(mealItemID);
    }
}
