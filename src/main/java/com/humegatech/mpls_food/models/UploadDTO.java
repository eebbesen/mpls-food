package com.humegatech.mpls_food.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO {
    private Long dealId;
    private byte[] image;
    private boolean verified;
}
