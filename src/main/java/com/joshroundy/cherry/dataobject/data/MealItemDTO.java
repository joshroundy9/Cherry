package com.joshroundy.cherry.dataobject.data;

import jakarta.validation.constraints.Size;
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
    @Size(max = 100, message = "Item name cannot exceed 100 characters")
    String itemName;
    Double itemCalories;
    Double itemProtein;
    Boolean aiGenerated;
}
