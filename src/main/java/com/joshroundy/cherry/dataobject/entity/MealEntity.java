package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

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
    Integer userID;
    Integer dateID;
    ZonedDateTime dateTime;
}
