package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.repository.DateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphsServiceTest {

    private DateRepository dateRepository;
    private GraphsService graphsService;

    @BeforeEach
    void setUp() {
        dateRepository = mock(DateRepository.class);
        graphsService = new GraphsService(dateRepository);
    }

    @Test
    void testGetGraphData() {
        Integer userId = 1;
        Long daysBack = 2L;
        Date today = new Date(System.currentTimeMillis());
        Date twoDaysAgo = new Date(today.getTime() - (2 * 24 * 60 * 60 * 1000));

        DateEntity entity1 = new DateEntity();
        entity1.setDate(twoDaysAgo);
        DateEntity entity2 = new DateEntity();
        entity2.setDate(today);

        when(dateRepository.findByUserIDAndDateBetween(eq(userId), any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(entity1, entity2));

        List<DateEntity> result = graphsService.getGraphData(userId, daysBack);

        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    void testGetHeatMapData() {
        Integer userId = 1;
        Long daysBack = 1L;
        Date today = new Date(System.currentTimeMillis());

        DateEntity entity = new DateEntity();
        entity.setDate(today);
        entity.setDailyCalories(100.0);
        entity.setDailyProtein(50.0);
        entity.setDailyWeight(0.0);

        when(dateRepository.findByUserIDAndDateBetween(eq(userId), any(Date.class), any(Date.class)))
                .thenReturn(List.of(entity));

        var result = graphsService.getHeatMapData(userId, daysBack);

        assertEquals(1, result.size());
        assertEquals("NUTRITION", result.get(0).getValue());
    }
}