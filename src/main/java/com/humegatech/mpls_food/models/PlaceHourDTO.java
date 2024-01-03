package com.humegatech.mpls_food.models;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.jetbrains.annotations.NotNull;

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
public class PlaceHourDTO {
    private long id;

    // using 'place' instead of 'place_id' for compatibility with thymeleaf dropdown
    // if desired fragments/forms could be modified to use 'place_id' but display 'place',
    // it just isn't baked in by default
    private Long place;
    @NotNull
    LocalTime openTime;
    @NotNull
    LocalTime closeTime;
    @NotNull DayOfWeek dayOfWeek;
}
