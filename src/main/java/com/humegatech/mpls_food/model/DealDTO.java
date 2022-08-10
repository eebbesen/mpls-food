package com.humegatech.mpls_food.model;

import com.humegatech.mpls_food.domain.Place;

import javax.validation.constraints.NotNull;


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

    public boolean isSunday() { return sunday; }

    public void setSunday(final boolean sunday) { this.sunday = sunday; }

    public boolean isMonday() { return monday; }

    public void setMonday(final boolean monday) { this.monday = monday; }

    public boolean isTuesday() { return tuesday; }

    public void setTuesday(final boolean tuesday) { this.tuesday = tuesday; }

    public boolean isWednesday() { return wednesday; }

    public void setWednesday(final boolean wednesday) { this.wednesday = wednesday; }

    public boolean isThursday() { return thursday; }

    public void setThursday(final boolean thursday) { this.thursday = thursday; }

    public boolean isFriday() { return friday; }

    public void setFriday(final boolean friday) { this.friday = friday; }

    public boolean isSaturday() { return saturday; }

    public void setSaturday(final boolean saturday) { this.saturday = saturday; }

    public Place getPlace() {
        return place;
    }

    public void setPlace(final Place place) {
        this.place = place;
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

    private static String concat(final String text, final String newValue){
        if (text.length() > 0) {
            return String.format("%s, %s", text, newValue);
        }

        return newValue;
    }

}
