package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.auth.UserResponseDTO;
import com.joshroundy.cherry.dataobject.data.*;
import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.entity.MealEntity;
import com.joshroundy.cherry.dataobject.entity.MealItemEntity;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.service.DataService;
import com.joshroundy.cherry.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/data")
@AllArgsConstructor
public class DataController {
    private DataService dataService;
    private UserService userService;

    @GetMapping("/user")
    public UserResponseDTO getUser(
            @RequestParam(value="username", required=true) String username,
            @RequestHeader(value="user-id", required=true) Integer userID) {
        var userEntity =  userService.loadUserEntityByUsername(username);
        if (!userEntity.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the requested username");
        }
        return userEntity;
    }

    @PostMapping("/user/delete-account")
    public ResponseEntity<?> deleteUserAccount(
            @RequestHeader(value="user-id", required=true) Integer userID) {
        try {
            userService.deleteUserAccount(userID);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error deleting user account: " + e.getMessage());
        }
        return ResponseEntity.ok("User account deleted successfully");
    }

    @PostMapping("/user/update-weight")
    public UserResponseDTO updateUserWeight(
            @RequestParam(value="weight", required=true) @Min(0) @Max(999) Double weight,
            @RequestHeader(value="user-id", required=true) Integer userID) {
        return userService.updateUserWeight(userID, weight);
    }

    @GetMapping("/date")
    public List<DateEntity> getDates(@RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.findDatesFromUserID(userID);
    }

    @GetMapping("/date/from-user-and-date")
    public DateEntity getDateFromUserIDAndDate(@RequestHeader(value="user-id", required=true) Integer userID,
                                                     @RequestParam(value="date", required=true) Date date) {
        return dataService.findDateFromUserIDAndDate(userID, date);
    }

    @GetMapping("/meal")
    public List<MealEntity> getMeals(@RequestParam(value="dateid", required=true) Integer dateID,
                                     @RequestHeader(value="user-id", required=true) Integer userID) {
        var mealEntityList = dataService.findMealsFromDateID(dateID);
        mealEntityList
                .forEach(mealEntity -> {
                    if (!mealEntity.getUserID().equals(userID)) {
                        throw new AccessDeniedException("User ID does not match the requested date ID");
                    }
                });
        return mealEntityList;
    }

    @GetMapping("/meal-item")
    public List<MealItemEntity> getMealItems(@RequestParam(value="mealid", required=true) Integer mealID,
                                             @RequestHeader(value="user-id", required=true) Integer userID) {
        var mealItemEntityList = dataService.findMealItemsFromMealID(mealID);
        mealItemEntityList
                .forEach(mealEntity -> {
                    if (!mealEntity.getUserID().equals(userID)) {
                        throw new AccessDeniedException("User ID does not match the requested meal ID");
                    }
                });
        return mealItemEntityList;
    }

    @GetMapping("/meal-item/recents")
    public List<MealItemEntity> getRecentMealItems(@RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.getMealItemRecents(userID, false);
    }

    @PostMapping("/date")
    public DateEntity createDate(@RequestBody DateDTO body,
                                 @RequestHeader(value="user-id", required=true) Integer userID) {
        if (!body.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the date owner.");
        }
        return dataService.createDate(body);
    }

    @PostMapping("/date/update-weight")
    public DateEntity updateDateWeight(@RequestParam(value="dateid", required=true) Integer dateID,
                                       @RequestParam(value="weight", required=true) @Min(0) @Max(999) Double weight,
                                       @RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.updateDateWeight(dateID, weight, userID);
    }

    @PostMapping("/date/update-nutrition")
    public DateEntity updateDateNutrition(@RequestParam(value="dateid", required=true) Integer dateID,
                                       @RequestParam(value="calories", required=true) Double calories,
                                          @RequestParam(value="protein", required=true) Double protein,
                                          @RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.updateDateNutrition(dateID, calories, protein, userID);
    }

    @DeleteMapping("/date/delete")
    public void updateDateNutrition(@RequestParam(value="dateid", required=true) Integer dateID,
                                          @RequestHeader(value="user-id", required=true) Integer userID) {
        dataService.deleteDate(dateID, userID);
    }

    @PostMapping("/meal")
    public MealEntity createMeal(@RequestBody @Valid MealDTO body,
                                 @RequestHeader(value="user-id", required=true) Integer userID) {
        if (!body.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the meal owner.");
        }
        return dataService.createMeal(body);
    }

    @PostMapping("/meal/update-time")
    public MealEntity updateMealTime(@RequestParam(value="mealid", required=true) Integer mealID,
                                     @RequestParam(value="time", required=true) Time time,
                                     @RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.updateMealTime(mealID, time, userID);
    }

    @PostMapping("/meal/update-nutrition")
    public MealEntity updateMealNutrition(@RequestParam(value="mealid", required=true) Integer mealID,
                                          @RequestParam(value="calories", required=true) Double calories,
                                          @RequestParam(value="protein", required=true) Double protein,
                                          @RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.updateMealNutrition(mealID, calories, protein, userID);
    }

    @DeleteMapping("/meal/delete")
    public void deleteMeal(@RequestParam(value="mealid", required=true) Integer mealID,
                           @RequestHeader(value="user-id", required=true) Integer userID) {
        dataService.deleteMeal(mealID, userID);
    }

    @PostMapping("/meal-item")
    public MealItemEntity createMealItem(@RequestBody @Valid MealItemDTO body,
                                         @RequestHeader(value="user-id", required=true) Integer userID) {
        if (!body.getUserID().equals(userID)) {
            throw new AccessDeniedException("User ID does not match the meal item owner.");
        }
        return dataService.createMealItem(body);
    }

    @PostMapping("/meal-item/update-name")
    public MealItemEntity updateMealItemName(@RequestParam(value="mealitemid", required=true) Integer mealItemID,
                                 @RequestParam(value="name", required=true) String name,
                                             @RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.updateMealItemName(mealItemID, name, userID);
    }

    @PostMapping("/meal-item/update-calories")
    public MealItemEntity updateMealItemCalories(@RequestParam(value="mealitemid", required=true) Integer mealItemID,
                                             @RequestParam(value="calories", required=true) Double calories,
                                                 @RequestParam(value="protein", required=true) Double protein,
                                                 @RequestHeader(value="user-id", required=true) Integer userID) {
        return dataService.updateMealItemNutrition(mealItemID, calories, protein, userID);
    }

    @DeleteMapping("/meal-item/delete")
    public void deleteMealItem(@RequestParam(value="mealitemid", required=true) Integer mealItemID,
                               @RequestHeader(value="user-id", required=true) Integer userID) {
        dataService.deleteMealItem(mealItemID, userID);
    }
}
