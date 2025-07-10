package com.joshroundy.cherry.client;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Disabled
public class GPTClientIntegrationTest {

    private GPTClient subject;

    @BeforeEach
    void setUp() {
        subject = new GPTClient();
    }

    @Test
    public void testGetGPTTextResponse() {
        var input = "One banana and two apples with two tablespoons of peanut butter.";
        var response = subject.getGPTTextResponse(input);
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        String REGEX = "^(True|False) \"\\d+\" \"\\d+\"$";
        assertThat(response.getBody().getChoices().get(0).getMessage().getContent().matches(REGEX)).isTrue();
    }
}
