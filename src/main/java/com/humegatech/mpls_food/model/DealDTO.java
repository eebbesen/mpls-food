package com.humegatech.mpls_food.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class DealDTO {

    private Long id;

    @NotNull
    private String description;

    @Size(max = 255)
    private String daysOfWeek;

    @NotNull
    private Long place;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(final String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Long getPlace() {
        return place;
    }

    public void setPlace(final Long place) {
        this.place = place;
    }

}
