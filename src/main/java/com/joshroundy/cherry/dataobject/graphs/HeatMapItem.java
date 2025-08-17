package com.joshroundy.cherry.dataobject.graphs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class HeatMapItem {
    private Date date;
    private String value;
}
