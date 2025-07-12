package com.joshroundy.cherry.dataobject.data;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MealDTO {
    @Max(value = 100, message = "Meal name cannot exceed 100 characters")
    String mealName;
    Integer userID;
    Integer dateID;
    Time time;
}
