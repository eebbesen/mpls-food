package com.humegatech.mpls_food.models;

import com.humegatech.mpls_food.domains.RewardType;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String address;

    private String truncatedAddress;
    private String website;
    private boolean app;
    private boolean orderAhead;
    private String rewardNotes;
    private RewardType rewardType;

}
