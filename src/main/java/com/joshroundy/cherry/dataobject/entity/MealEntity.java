package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "meal")
public class MealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer MealID;
    Integer UserID;
    Integer DateID;
    ZonedDateTime DateTime;
}
