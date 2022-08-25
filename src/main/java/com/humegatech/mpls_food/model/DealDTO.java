package com.humegatech.mpls_food.model;

import com.humegatech.mpls_food.domain.Place;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DealDTO {
    private Long id;

    @NotNull
    private Place place;

    @NotNull
    private String description;

    private boolean monday;
    private boolean sunday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
}
