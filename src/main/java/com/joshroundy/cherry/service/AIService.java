package com.joshroundy.cherry.service;

import com.joshroundy.cherry.client.AIClient;
import com.joshroundy.cherry.client.util.ClientUtil;
import com.joshroundy.cherry.dataobject.client.AIDataResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AIService {

    private AIClient AIClient;
    private ClientUtil clientUtil;

    public AIDataResponseDTO getNutritionData(String foodEntry) {
        var response = AIClient.getGPTTextResponse(foodEntry);
        if (response.getBody() == null || response.getBody().getChoices() == null || response.getBody().getChoices().isEmpty() || response.getBody().getChoices().get(0).getMessage() == null) {
            throw new RuntimeException("Invalid response from OpenAI TEXT API");
        }

        return clientUtil.mapGPTClientResponseToAIDataResponse(response.getBody().getChoices().get(0).getMessage().getContent(), foodEntry);
    }

    public AIDataResponseDTO getImageNutritionData(String imageBase64) {
        var response = AIClient.getGPTImageResponse(imageBase64);
        if (response.getBody() == null || response.getBody().getChoices() == null || response.getBody().getChoices().isEmpty() || response.getBody().getChoices().get(0).getMessage() == null) {
            throw new RuntimeException("Invalid response from OpenAI IMAGE API");
        }

        return clientUtil.mapGPTClientImageResponseToAIDataResponse(response.getBody().getChoices().get(0).getMessage().getContent());
    }

}
