package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.data.DateDTO;
import com.joshroundy.cherry.dataobject.data.MealDTO;
import com.joshroundy.cherry.dataobject.data.MealItemDTO;
import com.joshroundy.cherry.dataobject.entity.DateEntity;
import com.joshroundy.cherry.dataobject.entity.MealEntity;
import com.joshroundy.cherry.dataobject.entity.MealItemEntity;
import com.joshroundy.cherry.repository.DateRepository;
import com.joshroundy.cherry.repository.MealItemRepository;
import com.joshroundy.cherry.repository.MealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DataServiceTest {
    public static final Integer USER_ID = 3534;
    public static final Integer DATE_ID = 24612;
    public static final Integer MEAL_ID = 523512;
    public static final Integer MEAL_ITEM_ID = 1231250;
    public static final Double DAILY_WEIGHT = 178.3;
    public static final String MEAL_ITEM_NAME = "Cheeseburger";
    public static final Integer MEAL_ITEM_CALORIES = 550;
    public static final Date DATE = Date.valueOf("2024-08-20");
    public static final Time TIME = Time.valueOf("09:30");
    private DateEntity dateEntity;
    private MealEntity mealEntity;
    private MealItemEntity mealItemEntity;
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
        mealEntity = MealEntity.builder()
                .userID(USER_ID)
                .mealID(MEAL_ID)
                .dateID(DATE_ID)
                .time(TIME)
                .build();
        mealItemEntity = MealItemEntity.builder()
                .userID(USER_ID)
                .mealID(MEAL_ID)
                .itemID(MEAL_ITEM_ID)
                .itemName(MEAL_ITEM_NAME)
                .itemCalories(MEAL_ITEM_CALORIES)
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
    void findDateByUserIDAndDate() {
        when(dateRepository.findByUserID(USER_ID))
                .thenReturn(List.of(dateEntity, DateEntity.builder()
                        .date(Date.valueOf("2005-04-21")).build()));
        var actual = subject.findDateByUserIDAndDate(USER_ID, DATE);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(dateEntity);
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
                .thenAnswer(functionCall -> functionCall.getArguments()[0]);
        var actual = subject.updateDateWeight(DATE_ID, updatedWeight);
        assertThat(actual).isNotNull();
        assertThat(actual.getDailyWeight()).isEqualTo(updatedWeight);
    }
    @Test
    void findMealsFromDateID() {
        var expected = List.of(mealEntity);
        when(mealRepository.findByDateID(any())).thenReturn(expected);
        var actual = subject.findMealsFromDateID(DATE_ID);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void createMeal() {
        var mealDTO = MealDTO.builder()
                .userID(mealEntity.getUserID())
                .dateID(mealEntity.getDateID())
                .time(mealEntity.getTime())
                .build();
        when(mealRepository.save(any(MealEntity.class)))
                .then(functionCall ->
                        assertThat(functionCall.getArguments()[0])
                                .usingRecursiveComparison()
                                .ignoringFields("mealID")
                                .isEqualTo(mealEntity));
        when(mealRepository.save(any(MealEntity.class)))
                .thenReturn(mealEntity);
        var actual = subject.createMeal(mealDTO);
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(mealEntity);
    }
    @Test
    void updateMealTime() {
        var newTime = Time.valueOf("10:30");
        when(mealRepository.findById(any())).thenReturn(Optional.ofNullable(mealEntity));
        when(mealRepository.save(any(MealEntity.class)))
                .thenAnswer(functionCall -> functionCall.getArguments()[0]);
        var actual = subject.updateMealTime(DATE_ID, newTime);
        assertThat(actual).isNotNull();
        assertThat(actual.getTime()).isEqualTo(newTime);
    }
    @Test
    void findMealItemsFromMealID() {
        when(mealItemRepository.findByMealID(any()))
                .thenReturn(List.of(mealItemEntity));
        var actual = subject.findMealItemsFromMealID(MEAL_ID);
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual).contains(mealItemEntity);
    }
    @Test
    void createMealItem() {
        var mealItemDTO = MealItemDTO.builder()
                .userID(mealItemEntity.getUserID())
                .dateID(mealItemEntity.getDateID())
                .mealID(mealItemEntity.getMealID())
                .itemCalories(mealItemEntity.getItemCalories())
                .itemName(mealItemEntity.getItemName())
                .build();
        when(mealItemRepository.save(any(MealItemEntity.class)))
                .then(functionCall ->
                        assertThat(functionCall.getArguments()[0])
                                .usingRecursiveComparison()
                                .ignoringFields("mealItemID")
                                .isEqualTo(mealItemEntity));
        when(mealItemRepository.save(any(MealItemEntity.class)))
                .thenReturn(mealItemEntity);
        var actual = subject.createMealItem(mealItemDTO);
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(mealItemEntity);
    }
    @Test
    void updateMealItemName() {
        var newName = "Hamburger";
        when(mealItemRepository.findById(any())).thenReturn(Optional.ofNullable(mealItemEntity));
        when(mealItemRepository.save(any(MealItemEntity.class)))
                .thenAnswer(functionCall -> functionCall.getArguments()[0]);
        var actual = subject.updateMealItemName(MEAL_ITEM_ID, newName);
        assertThat(actual).isNotNull();
        assertThat(actual.getItemName()).isEqualTo(newName);
    }
    @Test
    void updateMealItemCalories() {
        var newCalories = 450;
        when(mealItemRepository.findById(any())).thenReturn(Optional.ofNullable(mealItemEntity));
        when(mealItemRepository.save(any(MealItemEntity.class)))
                .thenAnswer(functionCall -> functionCall.getArguments()[0]);
        var actual = subject.updateMealItemCalories(MEAL_ITEM_ID, newCalories);
        assertThat(actual).isNotNull();
        assertThat(actual.getItemCalories()).isEqualTo(newCalories);
    }
}
