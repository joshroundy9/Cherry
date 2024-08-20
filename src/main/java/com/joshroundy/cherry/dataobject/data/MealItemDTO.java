package com.joshroundy.cherry.dataobject.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MealItemDTO {
    Integer mealID;
    Integer dateID;
    Integer userID;
    String itemName;
    Integer itemCalories;
}
