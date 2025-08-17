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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
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
        return dateRepository.findByUserIDAndDate(userID, date).orElseGet(() ->
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
    public DateEntity updateDateWeight(Integer dateID, Double weight, Integer userID) {
        var dateEntity = dateRepository.findById(dateID).get();
        if (!dateEntity.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the date owner.");
        }
        dateEntity.setDailyWeight(weight);
        return dateRepository.save(dateEntity);
    }
    public DateEntity updateDateNutrition(Integer dateID, Double calories, Double protein, Integer userID) {
        var optionalDateEntity = dateRepository.findById(dateID);
        if (optionalDateEntity.isEmpty()) {
            return DateEntity.builder().build();
        }
        var dateEntity = optionalDateEntity.get();
        if (!dateEntity.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the date owner.");
        }
        dateEntity.setDailyCalories(calories);
        dateEntity.setDailyProtein(protein);
        return dateRepository.save(dateEntity);
    }
    public void deleteDate(Integer dateID, Integer userID) {
        dateRepository.findById(dateID).ifPresent(
                dateEntity -> {
                    if (!dateEntity.getUserID().equals(userID)) {
                        throw new AccessDeniedException("User ID does not match the date owner.");
                    }
                }
        );
        var mealsFromDateID = findMealsFromDateID(dateID);
        mealsFromDateID.forEach(mealEntity -> {
            mealItemRepository.deleteAll(findMealItemsFromMealID(mealEntity.getMealID()));
        });
        mealRepository.deleteAll(mealsFromDateID);
        dateRepository.deleteById(dateID);
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
    public MealEntity updateMealTime(Integer mealID, Time time, Integer userID) {
        var mealEntity = mealRepository.findById(mealID).get();
        if (!mealEntity.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the meal owner.");
        }
        mealEntity.setTime(time);
        return mealRepository.save(mealEntity);
    }
    public MealEntity updateMealNutrition(Integer mealID, Double calories, Double protein, Integer userID) {
        var mealEntity = mealRepository.findById(mealID).get();
        if (!mealEntity.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the meal owner.");
        }
        mealEntity.setMealCalories(calories);
        mealEntity.setMealProtein(protein);
        return mealRepository.save(mealEntity);
    }
    public void deleteMeal(Integer mealID, Integer userID) {
        mealRepository.findById(mealID).ifPresent(
                mealEntity -> {
                    if (!mealEntity.getUserID().equals(userID)) {
                        throw new AccessDeniedException("User ID does not match the meal owner.");
                    }
                }
        );
        mealItemRepository.deleteAll(findMealItemsFromMealID(mealID));
        mealRepository.deleteById(mealID);
    }
    /*Meal item methods*/
    public List<MealItemEntity> findMealItemsFromMealID(Integer mealID) {
        return mealItemRepository.findByMealID(mealID);
    }
    public List<MealItemEntity> getMealItemRecents(Integer userID, Boolean aiGenerated) {
        return mealItemRepository.findTop5ByUserIDAndAiGeneratedOrderByCreatedTSDesc(userID, aiGenerated);
    }
    public MealItemEntity createMealItem(MealItemDTO mealItemDTO) {
        var mealItem = mealItemRepository.save(MealItemEntity.builder()
                        .userID(mealItemDTO.getUserID())
                        .itemCalories(mealItemDTO.getItemCalories())
                        .itemProtein(mealItemDTO.getItemProtein())
                        .itemName(mealItemDTO.getItemName())
                        .dateID(mealItemDTO.getDateID())
                        .mealID(mealItemDTO.getMealID())
                        .aiGenerated(mealItemDTO.getAiGenerated())
                        .createdTS(new Timestamp(System.currentTimeMillis()))
                .build());
        mealRepository.updateMealTotalsByMealID(mealItemDTO.getMealID());
        dateRepository.updateDailyTotalsByDateID(mealItemDTO.getDateID());
        return mealItem;
    }
    public MealItemEntity updateMealItemName(Integer mealItemID, String mealItemName, Integer userID) {
        var mealItemEntity = mealItemRepository.findById(mealItemID).get();
        if (!mealItemEntity.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the meal item owner.");
        }
        mealItemEntity.setItemName(mealItemName);
        return mealItemRepository.save(mealItemEntity);
    }
    public MealItemEntity updateMealItemNutrition(Integer mealItemID, Double mealItemCalories, Double mealItemProtein, Integer userID) {
        var mealItemEntity = mealItemRepository.findById(mealItemID).get();
        if (!mealItemEntity.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the meal item owner.");
        }
        mealItemEntity.setItemCalories(mealItemCalories);
        mealItemEntity.setItemProtein(mealItemProtein);
        return mealItemRepository.save(mealItemEntity);
    }
    public void deleteMealItem(Integer mealItemID, Integer userID) {
        var mealItemOptional = mealItemRepository.findById(mealItemID);
        mealItemOptional.ifPresent(mealItem -> {
                    if (!mealItem.getUserID().equals(userID)) {
                        throw new AccessDeniedException("User ID does not match the meal item owner.");
                    }
                }
        );
        var mealItem = mealItemOptional.get();
        mealItemRepository.deleteById(mealItemID);
        mealRepository.updateMealTotalsByMealID(mealItem.getMealID());
        dateRepository.updateDailyTotalsByDateID(mealItem.getDateID());
    }
}
