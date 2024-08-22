package com.joshroundy.cherry.dataobject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "date")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer dateID;
    @Column(nullable = false)
    Integer userID;
    Date date;
    Double dailyWeight;
}
