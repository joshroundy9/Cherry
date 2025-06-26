package com.joshroundy.cherry.dataobject.client;

import com.joshroundy.cherry.constant.ClientConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

import static com.joshroundy.cherry.constant.ClientConstants.TEXT_RESPONSE_REGEX;

@Getter
@Setter
public class GPTClientResponseDTO {
    String object;
    List<GPTClientChoiceDTO> choices;
}

