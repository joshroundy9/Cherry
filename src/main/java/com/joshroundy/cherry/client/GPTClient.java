package com.joshroundy.cherry.client;

import com.joshroundy.cherry.client.util.AIUtil;
import com.joshroundy.cherry.dataobject.client.GPTClientResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.joshroundy.cherry.constant.ClientConstants.OPENAI_API_URL;

@Component
public class GPTClient {
    private final AIUtil aiUtil = new AIUtil();

    public ResponseEntity<GPTClientResponseDTO> getGPTTextResponse(String input) {
        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiUtil.getAPIKey());

        var body = aiUtil.createTextRequestBody(input);

        var entity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(OPENAI_API_URL, entity, GPTClientResponseDTO.class);
    }
}
