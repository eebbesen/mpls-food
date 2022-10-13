package com.humegatech.mpls_food.models;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DayDTO {
    private Long id;

    // using 'deal' instead of 'deal_id' for compatibility with thymeleaf dropdown
    // if desired fragments/forms could be modified to use 'deal_id' but display 'deal', it just isn't baked in by default
    @NotNull
    private Long deal;
    private String dealDescription;
    private String placeName;
    private DayOfWeek dayOfWeek;
    private LocalDate date;
    private String dayOfWeekDisplay;
    private String dish;
    private String cuisine;
    private String priceRange;
    private String discountRange;
    private String discountPercentRange;
    private Double minPrice;
    private Double minDiscount;
    private Double minDiscountPercent;
    private String startTime;
    private String endTime;
    private boolean verified;
}
