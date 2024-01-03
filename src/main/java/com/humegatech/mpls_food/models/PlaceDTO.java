package com.humegatech.mpls_food.models;

import java.util.Set;

import com.humegatech.mpls_food.domains.RewardType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Set<PlaceHourDTO> placeHours;

}
