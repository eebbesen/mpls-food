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

    private boolean sunday;

    private boolean monday;

    private boolean tuesday;

    private boolean wednesday;

    private boolean thursday;

    private boolean friday;

    private boolean saturday;

    private static String concat(final String text, final String newValue) {
        if (text.length() > 0) {
            return String.format("%s, %s", text, newValue);
        }

        return newValue;
    }

    public String daysActive() {
        String daysActive = "";

        if (sunday) {
            daysActive = concat(daysActive, "Sunday");
        }

        if (monday) {
            daysActive = concat(daysActive, "Monday");
        }

        if (tuesday) {
            daysActive = concat(daysActive, "Tuesday");
        }

        if (wednesday) {
            daysActive = concat(daysActive, "Wednesday");
        }

        if (thursday) {
            daysActive = concat(daysActive, "Thursday");
        }

        if (friday) {
            daysActive = concat(daysActive, "Friday");
        }

        if (saturday) {
            daysActive = concat(daysActive, "Saturday");
        }

        return daysActive;
    }

}
