package com.joshroundy.cherry.dataobject.client;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GPTClientChoiceDTO {
    GPTClientMessageDTO message;
}
