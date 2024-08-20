package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.data.DateDTO;
import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.repository.DateRepository;
import com.joshroundy.cherry.repository.MealItemRepository;
import com.joshroundy.cherry.repository.MealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DataServiceTest {
    public static Integer USER_ID = 3534;
    public static Integer DATE_ID = 24612;
    public static Double DAILY_WEIGHT = 178.3;
    public static Date DATE = Date.valueOf("2024-08-20");
    private DateEntity dateEntity;
    @InjectMocks
    DataService subject;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private MealItemRepository mealItemRepository;
    @Mock
    private DateRepository dateRepository;
    @BeforeEach
    void setUp() {
        dateEntity = DateEntity.builder()
                .dateID(DATE_ID)
                .date(DATE)
                .userID(USER_ID)
                .dailyWeight(DAILY_WEIGHT)
                .build();
    }
    @Test
    void findDatesFromUserID() {
        when(dateRepository.findByUserID(any()))
                .thenReturn(List.of(DateEntity.builder().build()));
        var actual = subject.findDatesFromUserID(USER_ID);
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
    }
    @Test
    void createDate() {
        var dateDTO = DateDTO.builder()
                .date(DATE)
                .userID(USER_ID)
                .dailyWeight(DAILY_WEIGHT)
                .build();
        when(dateRepository.save(any(DateEntity.class))).thenReturn(dateEntity);
        var actual = subject.createDate(dateDTO);
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(dateEntity);
    }
    @Test
    void updateDateWeight() {
        var updatedWeight = 180.34;
        when(dateRepository.findById(any())).thenReturn(Optional.ofNullable(dateEntity));
        when(dateRepository.save(any(DateEntity.class)))
                .thenAnswer(dateEntity -> dateEntity.getArguments()[0]);
        var actual = subject.updateDateWeight(DATE_ID, updatedWeight);
        assertThat(actual).isNotNull();
        assertThat(actual.getDailyWeight()).isEqualTo(updatedWeight);
    }

}
