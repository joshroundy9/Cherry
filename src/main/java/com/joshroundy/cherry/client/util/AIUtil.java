package com.joshroundy.cherry.client.util;

import com.joshroundy.cherry.constant.ClientConstants;
import com.joshroundy.cherry.dataobject.client.AIDataResponseDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.joshroundy.cherry.constant.ClientConstants.TEXT_DEVELOPER_PROMPT;
import static com.joshroundy.cherry.constant.ClientConstants.TEXT_MODEL;

@Component
public class AIUtil {

    public String getAPIKey() {
        try {
        return Files.readString(Paths.get(ClientConstants.OPENAI_API_KEY_PATH)).trim();
        } catch (IOException e) {
            return "";
        }
    }

    public String createTextRequestBody(String input) {
        return """
                {
                  "model": "%s",
                  "store": true,
                  "messages": [
                     {"role": "developer", "content": "%s"},
                     {"role": "user", "content": "%s"}
                   ]
                }
                """.formatted(TEXT_MODEL, TEXT_DEVELOPER_PROMPT, input);
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
}
