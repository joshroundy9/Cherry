package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "date")
public class DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer DateID;
    @Column(nullable = false)
    Integer UserID;
    Date Date;
    Double DailyWeight;
}
