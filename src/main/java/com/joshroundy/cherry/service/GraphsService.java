package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.graphs.HeatMapItem;
import com.joshroundy.cherry.repository.DateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GraphsService {
    private DateRepository dateRepository;

    public List<DateEntity> getGraphData(Integer userID, Long daysBack) {
        var today = new Date(System.currentTimeMillis());
        var variableDaysAgo = new Date(today.getTime() - (daysBack * 24 * 60 * 60 * 1000));

        return dateRepository.findByUserIDAndDateBetween(userID, variableDaysAgo, today);
    }
    /**
     * Retrieves heat map tracking data for the user.
     * The heat map data is a Map of Date to String representing the tracking history.
     * @return Map of Date to String representing the heat map data.
     */
    public List<HeatMapItem> getHeatMapData(Integer userID, Long daysBack) {
        return getGraphData(userID, daysBack).stream()
                .map(dateEntity -> {
                    var trackingStatus = "NONE";
                    if (dateEntity.getDailyCalories() > 0 || dateEntity.getDailyProtein() > 0) {
                        trackingStatus = "NUTRITION";
                    }
                    if (dateEntity.getDailyWeight() > 0) {
                        if (trackingStatus.equals("NUTRITION")) {
                            trackingStatus = "BOTH";
                        } else {
                            trackingStatus = "WEIGHT";
                        }
                    }
                    return HeatMapItem.builder()
                            .date(dateEntity.getDate())
                            .value(trackingStatus)
                            .build();
                })
                .sorted(Comparator.comparing(HeatMapItem::getDate))
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageData(Integer userID) {
        var graphData = dateRepository.findByUserID(userID);
        double totalCalories = 0.0;
        double totalProtein = 0.0;
        double totalWeight = 0.0;
        int weightCount = 0;
        int nutritionCount = 0;

        for (DateEntity dateEntity : graphData) {
            if (dateEntity.getDailyCalories() > 0 || dateEntity.getDailyProtein() > 0) {
                nutritionCount++;
                totalCalories += dateEntity.getDailyCalories();
                totalProtein += dateEntity.getDailyProtein();
            }
            if (dateEntity.getDailyWeight() > 0) {
                weightCount++;
                totalWeight += dateEntity.getDailyWeight();
            }
        }

        return Map.of(
                "averageCalories", nutritionCount > 0 ? totalCalories / nutritionCount : 0.0,
                "averageProtein", nutritionCount > 0 ? totalProtein / nutritionCount : 0.0,
                "averageWeight", weightCount > 0 ? totalWeight / weightCount : 0.0
        );
    }
}
