package com.joshroundy.cherry.dataobject.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MealDTO {
    Integer userID;
    Integer dateID;
    ZonedDateTime dateTime;
}
