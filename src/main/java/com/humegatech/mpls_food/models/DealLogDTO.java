package com.humegatech.mpls_food.models;

import com.humegatech.mpls_food.domains.DealType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealLogDTO {
    private Long id;
    private String description;
    private DealType dealType;
    private Boolean redeemed;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate redemptionDate;
    // using 'place' and 'deal' instead of 'place_id' and 'deal_id' for compatibility with thymeleaf dropdown
    private Long place;
    private Long deal;
    private String placeName;
    private String dealDescription;
}
