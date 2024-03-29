package com.humegatech.mpls_food.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.humegatech.mpls_food.domains.DealType;

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
public class DealDTO {
    private Long id;

    // using 'place' instead of 'place_id' for compatibility with thymeleaf dropdown
    // if desired fragments/forms could be modified to use 'place_id' but display 'place',
    // it just isn't baked in by default
    private Long place;
    private String placeName;
    private String description;
    private boolean monday;
    private boolean sunday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private String daysDisplay;
    private String dish;
    private Double minPrice;
    private Double maxPrice;
    private Double minDiscount;
    private Double maxDiscount;
    private Double minDiscountPercent;
    private Double maxDiscountPercent;
    private String priceRange;
    private String discountRange;
    private String discountPercentRange;
    private boolean verified;
    private boolean happyHour;
    private boolean timeBoxed;
    private boolean taxIncluded;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private DealType dealType;
    @Builder.Default
    private List<UploadDTO> uploads = new ArrayList<>();
}
