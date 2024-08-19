package com.joshroundy.cherry.service;

import com.joshroundy.cherry.repository.DateRepository;
import com.joshroundy.cherry.repository.MealItemRepository;
import com.joshroundy.cherry.repository.MealRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DateService {
    private DateRepository dateRepository;
    private MealRepository mealRepository;
    private MealItemRepository mealItemRepository;


}
