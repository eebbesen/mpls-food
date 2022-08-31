package com.humegatech.mpls_food.models;

import com.humegatech.mpls_food.domains.Deal;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealDayDTO {
    private Long id;
    @NotNull
    private Deal deal;

    private DayOfWeek dayOfWeek;
    private LocalDate date;
    private String dayOfWeekDisplay;
}
