package com.joshroundy.cherry.dataobject.graphs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Map;

@Getter
@Setter
@Builder
public class HeatMapDataDTO {
    private Map<Date, String> heatMapData;
}
