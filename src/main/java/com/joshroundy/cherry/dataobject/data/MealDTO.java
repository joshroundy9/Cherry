package com.joshroundy.cherry.dataobject.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MealDTO {
    Integer userID;
    Integer dateID;
    Time time;
}
