package com.joshroundy.cherry.client;

import com.joshroundy.cherry.client.util.ClientUtil;
import com.joshroundy.cherry.dataobject.client.CaptchaClientResponseDTO;
import com.joshroundy.cherry.dataobject.client.GPTClientResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.joshroundy.cherry.constant.ClientConstants.CAPTCHA_VERIFICATION_URL;

@Component
public class CaptchaClient {
    @Value("${recaptcha.secret.key}")
    private String recaptchaSecretKey;

    public ResponseEntity<CaptchaClientResponseDTO> getCaptchaVerificationResponse(String token) {
        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", recaptchaSecretKey);
        body.add("response", token);

        var entity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(CAPTCHA_VERIFICATION_URL, entity, CaptchaClientResponseDTO.class);
    }
}
