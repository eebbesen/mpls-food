package com.humegatech.mpls_food.model;

import com.humegatech.mpls_food.domain.Place;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Getter
@Setter
public class DealDTO {

    @NotNull
    private Long id;

    @NotNull
    private Place place;

    @NotNull
    private String description;

    private DayOfWeek dayOfWeek;
}
