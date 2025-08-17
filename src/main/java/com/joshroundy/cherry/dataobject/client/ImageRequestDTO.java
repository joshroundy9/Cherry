package com.joshroundy.cherry.dataobject.client;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDTO {
    private String imageBase64;
}
