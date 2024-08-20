package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.data.*;
import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.entity.MealEntity;
import com.joshroundy.cherry.dataobject.entity.MealItemEntity;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.service.DataService;
import com.joshroundy.cherry.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/data")
@AllArgsConstructor
public class DataController {
    private DataService dataService;
    private UserService userService;

    @GetMapping("/user")
    public UserEntity getUser(@RequestParam(value="username", required=true) String username) {
        return userService.loadUserEntityByUsername(username);
    }
    @GetMapping("/date")
    public List<DateEntity> getDates(@RequestParam(value="userid", required=true) Integer userID) {
        return dataService.findDatesFromUserID(userID);
    }
    @GetMapping("/meal")
    public List<MealEntity> getMeals(@RequestParam(value="dateid", required=true) Integer dateID) {
        return dataService.findMealsFromDateID(dateID);
    }
    @GetMapping("/meal-item")
    public List<MealItemEntity> getMealItems(@RequestParam(value="mealid", required=true) Integer mealID) {
        return dataService.findMealItemsFromMealID(mealID);
    }

    @PostMapping("/date")
    public DateEntity createDate(@RequestBody DateDTO body) {
        return dataService.createDate(body);
    }
    @PostMapping("/date/update-weight")
    public DateEntity updateDateWeight(@RequestParam(value="dateid", required=true) Integer dateID,
                                       @RequestParam(value="weight", required=true) Double weight) {
        return dataService.updateDateWeight(dateID, weight);
    }
    @PostMapping("/meal")
    public MealEntity updateMealTime(@RequestBody MealDTO body) {
        return dataService.createMeal(body);
    }
    @PostMapping("/meal/update-time")
    public MealEntity updateMealTime(@RequestParam(value="mealid", required=true) Integer mealID,
                                     @RequestParam(value="datetime", required=true) ZonedDateTime dateTime) {
        return dataService.updateMealTime(mealID, dateTime);
    }
    @DeleteMapping("/meal/delete")
    public void deleteMeal(@RequestParam(value="mealid", required=true) Integer mealID) {
        dataService.deleteMeal(mealID);
    }
    @PostMapping("/meal-item")
    public MealItemEntity createMealItem(@RequestBody MealItemDTO body) {
        return dataService.createMealItem(body);
    }
    @PostMapping("/meal-item/update-name")
    public MealItemEntity updateMealItemName(@RequestParam(value="mealitemid", required=true) Integer mealItemID,
                                 @RequestParam(value="name", required=true) String name) {
        return dataService.updateMealItemName(mealItemID, name);
    }
    @PostMapping("/meal-item/update-calories")
    public MealItemEntity updateMealItemCalories(@RequestParam(value="mealitemid", required=true) Integer mealItemID,
                                             @RequestParam(value="calories", required=true) Integer calories) {
        return dataService.updateMealItemCalories(mealItemID, calories);
    }
    @DeleteMapping("/meal-item/delete")
    public void deleteMealItem(@RequestParam(value="mealitemid", required=true) Integer mealItemID) {
        dataService.deleteMealItem(mealItemID);
    }

}
