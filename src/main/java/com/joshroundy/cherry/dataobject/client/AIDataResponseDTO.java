package com.joshroundy.cherry.dataobject.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AIDataResponseDTO {
    String foodEntry;
    Boolean isValidEntry;
    Double calories;
    Double protein;
}
