package com.humegatech.mpls_food.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
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
public class DayDTO {
    private Long id;

    // using 'deal' instead of 'deal_id' for compatibility with thymeleaf dropdown
    // if desired fragments/forms could be modified to use 'deal_id' but display 'deal',
    // it just isn't baked in by default
    @NotNull
    private Long deal;
    private String dealDescription;
    private String placeName;
    private DayOfWeek dayOfWeek;
    private LocalDate date;
    private String dayOfWeekDisplay;
    private String dish;
    private String priceRange;
    private String discountRange;
    private String discountPercentRange;
    private Double minPrice;
    private Double minDiscount;
    private Double minDiscountPercent;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean verified;
    private boolean happyHour;
    private boolean timeBoxed;
}
