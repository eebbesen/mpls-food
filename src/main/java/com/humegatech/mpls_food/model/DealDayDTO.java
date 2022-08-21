package com.humegatech.mpls_food.model;

import com.humegatech.mpls_food.domain.Deal;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
public class DealDayDTO {
    @NotNull
    private Long id;

    @NotNull
    private Deal deal;

    private DayOfWeek dayOfWeek;
    private LocalDate date;
}
