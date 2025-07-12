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
import java.sql.Time;
import java.util.List;

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
    public DateEntity findDateFromUserIDAndDate(Integer userID, Date date) {
        return dateRepository.findByUserIDAndDate(userID, date).orElse(
                createDate(DateDTO.builder()
                        .userID(userID)
                        .date(date)
                        .dailyWeight(0.0)
                        .dailyCalories(0.0)
                        .dailyProtein(0.0)
                        .build())
        );
    }
    public DateEntity createDate(DateDTO dateDTO) {
        return dateRepository.save(
                DateEntity.builder()
                        .date(dateDTO.getDate())
                        .userID(dateDTO.getUserID())
                        .dailyWeight(dateDTO.getDailyWeight())
                        .dailyCalories(0.0)
                        .dailyProtein(0.0)
                        .build()
        );
    }
    public DateEntity updateDateWeight(Integer dateID, Double weight) {
        var dateEntity = dateRepository.findById(dateID).get();
        dateEntity.setDailyWeight(weight);
        return dateRepository.save(dateEntity);
    }
    public DateEntity updateDateNutrition(Integer dateID, Double calories, Double protein) {
        var dateEntity = dateRepository.findById(dateID).get();
        dateEntity.setDailyCalories(calories);
        dateEntity.setDailyProtein(protein);
        return dateRepository.save(dateEntity);
    }
    /*Meal methods*/
    public List<MealEntity> findMealsFromDateID(Integer dateID) {
        return mealRepository.findByDateID(dateID);
    }
    public MealEntity createMeal(MealDTO mealDTO) {
        return mealRepository.save(MealEntity.builder()
                        .userID(mealDTO.getUserID())
                        .dateID(mealDTO.getDateID())
                        .time(mealDTO.getTime())
                        .mealName(mealDTO.getMealName())
                        .mealCalories(0.0)
                        .mealProtein(0.0)
                .build());
    }
    public MealEntity updateMealTime(Integer mealID, Time time) {
        var mealEntity = mealRepository.findById(mealID).get();
        mealEntity.setTime(time);
        return mealRepository.save(mealEntity);
    }
    public MealEntity updateMealNutrition(Integer mealID, Double calories, Double protein) {
        var mealEntity = mealRepository.findById(mealID).get();
        mealEntity.setMealCalories(calories);
        mealEntity.setMealProtein(protein);
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
                        .itemProtein(mealItemDTO.getItemProtein())
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
    public MealItemEntity updateMealItemNutrition(Integer mealItemID, Double mealItemCalories, Double mealItemProtein) {
        var mealItemEntity = mealItemRepository.findById(mealItemID).get();
        mealItemEntity.setItemCalories(mealItemCalories);
        mealItemEntity.setItemProtein(mealItemProtein);
        return mealItemRepository.save(mealItemEntity);
    }
    public void deleteMealItem(Integer mealItemID) {
        mealItemRepository.deleteById(mealItemID);
    }
}
