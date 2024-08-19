package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class MealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer MealID;
    Integer UserID;
    Integer DateID;
    ZonedDateTime DateTime;
}
