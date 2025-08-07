package com.joshroundy.cherry.client.util;

import com.joshroundy.cherry.dataobject.client.AIDataResponseDTO;
import org.springframework.stereotype.Component;

import static com.joshroundy.cherry.constant.ClientConstants.*;

@Component
public class ClientUtil {
    public String createAIClientTextRequestBody(String input) {
        return """
                {
                  "model": "%s",
                  "store": true,
                  "messages": [
                     {"role": "developer", "content": "%s"},
                     {"role": "user", "content": "%s"}
                   ]
                }
                """.formatted(AI_MODEL, TEXT_DEVELOPER_PROMPT, input);
    }

    public String createAIClientImageRequestBody(String imageBase64) {
        return """
        {
          "model": "%s",
          "messages": [
             {
                "role": "user",
                "content": [
                    {
                        "type": "text",
                        "text": "%s"
                    },
                    {
                        "type": "image_url",
                        "image_url": {
                            "url": "data:image/jpeg;base64,%s"
                        }
                    }
                ]
             }
           ]
        }
        """.formatted(AI_MODEL, IMAGE_DEVELOPER_PROMPT, imageBase64);
    }

    public AIDataResponseDTO mapGPTClientResponseToAIDataResponse(String gptResponseContent, String foodEntry) {
        var parts = gptResponseContent.split(" ");
        if (parts.length != 3) {
            return AIDataResponseDTO.builder()
                    .isValidEntry(false)
                    .build();
        }
        return AIDataResponseDTO.builder()
                .foodEntry(foodEntry)
                .isValidEntry(parts[0].equals("True"))
                .calories(Double.parseDouble(parts[1]
                        .replaceAll("\"", "")
                        .replaceAll(",", "")))
                .protein(Double.parseDouble(parts[2]
                        .replaceAll("\"", "")
                        .replaceAll(",", "")))
                .build();
    }

    public AIDataResponseDTO mapGPTClientImageResponseToAIDataResponse(String gptResponseContent) {
        var parts = gptResponseContent.split("\"");
        return AIDataResponseDTO.builder()
                .foodEntry(parts[1]
                        .replaceAll("\"", "")
                        .replaceAll(",", ""))
                .isValidEntry(parts[0].equals("True "))
                .calories(Double.parseDouble(parts[3]
                        .replaceAll("\"", "")
                        .replaceAll(",", "")))
                .protein(Double.parseDouble(parts[5]
                        .replaceAll("\"", "")
                        .replaceAll(",", "")))
                .build();
    }
}
