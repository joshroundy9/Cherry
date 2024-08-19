package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
