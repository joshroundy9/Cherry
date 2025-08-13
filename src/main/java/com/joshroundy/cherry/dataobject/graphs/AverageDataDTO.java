package com.joshroundy.cherry.dataobject.graphs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class AverageDataDTO {
    private Map<String, Double> averageData;
}
