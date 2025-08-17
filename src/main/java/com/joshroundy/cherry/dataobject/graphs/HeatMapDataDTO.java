package com.joshroundy.cherry.dataobject.graphs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HeatMapDataDTO {
    private List<HeatMapItem> heatMapData;
}