package com.joshroundy.cherry.client;

import com.joshroundy.cherry.client.util.ClientUtil;
import com.joshroundy.cherry.dataobject.client.GPTClientResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.joshroundy.cherry.constant.ClientConstants.OPENAI_API_URL;

@Component
public class GPTClient {
    private final ClientUtil clientUtil = new ClientUtil();
    @Value("${openai_api_key}")
    private String apiKey;

    public ResponseEntity<GPTClientResponseDTO> getGPTTextResponse(String input) {
        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        var body = clientUtil.createAIClientTextRequestBody(input);

        var entity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(OPENAI_API_URL, entity, GPTClientResponseDTO.class);
    }

    public ResponseEntity<GPTClientResponseDTO> getGPTImageResponse(String imageBase64) {
        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        var body = clientUtil.createAIClientImageRequestBody(imageBase64);

        var entity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(OPENAI_API_URL, entity, GPTClientResponseDTO.class);
    }
}
