package com.humegatech.mpls_food.models;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO {
    private Long id;
    private Long dealId;
    private byte[] image;
    private boolean verified;
    private String createdBy;
    private OffsetDateTime dateCreated;
    private String dealDescription;
}
