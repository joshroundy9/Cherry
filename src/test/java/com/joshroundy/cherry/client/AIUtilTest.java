package com.joshroundy.cherry.client;

import com.joshroundy.cherry.client.util.AIUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AIUtilTest {

    private AIUtil subject = new AIUtil();

    @Test
    public void testCreateTextRequestBody() {
        String input = "One banana and two apples with two tablespoons of peanut butter.";
        String requestBody = subject.createTextRequestBody(input);
        assertNotNull(requestBody);
        assertEquals(("{\n" +
                "  \"model\": \"gpt-4o-mini\",\n" +
                "  \"store\": true,\n" +
                "  \"messages\": [\n" +
                "     {\"role\": \"developer\", \"content\": \"Respond to the input only with true or false if it is a valid food or beverage followed by two numbers about the specified food or beverage in separate quotes: the number of calories and the total grams of protein\"},\n" +
                "     {\"role\": \"user\", \"content\": \"One banana and two apples with two tablespoons of peanut butter.\"}\n" +
                "   ]\n" +
                "}").trim(), requestBody.trim());
    }

    @Test
    public void testMapGPTClientResponseToAIDataResponse() {
        var gptResponseContent = "True \"450\" \"12\"";
        var foodEntry = "One banana and two apples with two tablespoons of peanut butter.";
        var response = subject.mapGPTClientResponseToAIDataResponse(gptResponseContent, foodEntry);
        assertThat(response).isNotNull();
        assertThat(response.getIsValidEntry()).isTrue();
        assertThat(response.getCalories()).isEqualTo(450);
        assertThat(response.getProtein()).isEqualTo(12);
        assertThat(response.getFoodEntry()).isEqualTo(foodEntry);
    }
}
