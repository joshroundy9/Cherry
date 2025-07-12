package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.client.AIDataResponseDTO;
import com.joshroundy.cherry.service.AIService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/nutritiondata")
    public AIDataResponseDTO getNutritionData(
            @RequestHeader("Food-Entry")
            @Size(max = 100, message = "Entry text cannot exceed 100 characters")
            String foodEntry) {
        return aiService.getNutritionData(foodEntry);
    }
}
