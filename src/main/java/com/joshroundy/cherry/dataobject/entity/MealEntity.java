package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.sql.Time;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meal")
public class MealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer mealID;
    String mealName;
    Integer userID;
    Integer dateID;
    Time time;
}
