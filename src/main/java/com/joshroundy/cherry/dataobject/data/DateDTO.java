package com.joshroundy.cherry.dataobject.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DateDTO {
    Integer UserID;
    Date Date;
    Double DailyWeight;
}
