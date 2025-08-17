package com.joshroundy.cherry.dataobject.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(0) @Max(100000)
    Double itemCalories;
    @Min(0) @Max(10000)
    Double itemProtein;
    Boolean aiGenerated;
}
