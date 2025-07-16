package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.repository.DateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class GraphsService {
    private DateRepository dateRepository;

    public List<DateEntity> getGraphData(Integer userID, Long daysBack) {
        var today = new Date(System.currentTimeMillis());
        var variableDaysAgo = new Date(today.getTime() - (daysBack * 24 * 60 * 60 * 1000));

        return dateRepository.findByUserIDAndDateBetween(userID, variableDaysAgo, today);
    }
}
