package com.joshroundy.cherry.dataobject.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class CaptchaClientResponseDTO {
    private boolean success;
    private String challenge_ts;
    private String hostname;
    private List<String> errorCodes;
}
