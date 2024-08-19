package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "meal_item")
public class MealItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ItemID;
    Integer MealID;
    Integer DateID;
    Integer UserID;
    String ItemName;
    String ItemCalories;
}
