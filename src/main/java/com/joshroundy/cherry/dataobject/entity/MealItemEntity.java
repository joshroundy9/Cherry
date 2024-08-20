package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "meal_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer itemID;
    Integer mealID;
    Integer dateID;
    Integer userID;
    String itemName;
    Integer itemCalories;
}
