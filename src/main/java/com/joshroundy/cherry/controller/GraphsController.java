package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.graphs.AverageDataDTO;
import com.joshroundy.cherry.dataobject.graphs.HeatMapDataDTO;
import com.joshroundy.cherry.service.GraphsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graphs")
@AllArgsConstructor
public class GraphsController {
    private final GraphsService graphsService;

    @GetMapping("/data")
    public List<DateEntity> getGraphData(
            @RequestHeader(value = "user-id") Integer userID,
            @RequestParam(value = "daysback", required = false, defaultValue = "30")
            @Min(value = 0)
            @Max(value = 365) Long daysBack) {
        return graphsService.getGraphData(userID, daysBack);
    }

    @GetMapping("/heatmap")
    public HeatMapDataDTO getHeatMapData(
            @RequestHeader(value = "user-id") Integer userID,
            @RequestParam(value = "daysback", required = false, defaultValue = "30")
            @Min(value = 0)
            @Max(value = 365) Long daysBack) {
        return HeatMapDataDTO.builder()
                .heatMapData(graphsService.getHeatMapData(userID, daysBack))
                .build();
    }

    @GetMapping("/average")
    public AverageDataDTO getAverageData(@RequestHeader(value = "user-id") Integer userID) {
        return AverageDataDTO.builder()
                .averageData(graphsService.getAverageData(userID))
                .build();
    }
}
