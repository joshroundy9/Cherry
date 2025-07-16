package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.service.GraphsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
