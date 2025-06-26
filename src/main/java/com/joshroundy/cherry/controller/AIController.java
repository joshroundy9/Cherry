package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.client.AIDataResponseDTO;
import com.joshroundy.cherry.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/nutritiondata")
    public AIDataResponseDTO getNutritionData(String foodEntry) {
        return aiService.getNutritionData(foodEntry);
    }
}
