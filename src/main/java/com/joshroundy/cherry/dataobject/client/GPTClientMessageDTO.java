package com.joshroundy.cherry.dataobject.client;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.joshroundy.cherry.constant.ClientConstants.TEXT_RESPONSE_REGEX;

@Getter
@Setter
public class GPTClientMessageDTO {
    @Pattern(regexp = TEXT_RESPONSE_REGEX, message = "Content must contain True/False followed by two space-separated numbers in quotes")
    String content;

}
