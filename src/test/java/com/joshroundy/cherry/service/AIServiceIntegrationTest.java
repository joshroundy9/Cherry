package com.joshroundy.cherry.service;

import com.joshroundy.cherry.client.GPTClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
public class AIServiceIntegrationTest {
    @Autowired
    private AIService subject;

    @Test
    public void testGetGPTTextResponse() {
        var input = "One banana and two apples with two tablespoons of peanut butter.";
        var response = subject.getNutritionData(input);
        assertThat(response.getIsValidEntry()).isTrue();
        assertThat(response.getCalories()).isGreaterThan(0);
        assertThat(response.getProtein()).isGreaterThan(0);
    }
}
