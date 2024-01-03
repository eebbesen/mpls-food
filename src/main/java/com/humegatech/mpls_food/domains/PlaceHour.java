package com.humegatech.mpls_food.domains;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "place_hours")
@EntityListeners(AuditingEntityListener.class)
public class PlaceHour extends BaseEntity {
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "place_id", foreignKey = @ForeignKey(name = "fk_place_hours_places"))
    private Place place;
    private DayOfWeek dayOfWeek;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceHour placeHour = (PlaceHour) o;
        return Objects.equals(place, placeHour.place) &&
                              dayOfWeek == placeHour.dayOfWeek &&
                              Objects.equals(openTime, placeHour.openTime) &&
            Objects.equals(closeTime, placeHour.closeTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, dayOfWeek, openTime, closeTime);
    }

    @Override
    public String toString() {
        return "PlaceHour{" +
                "place=" + place +
                ", dayOfWeek=" + dayOfWeek +
                ", open=" + openTime +
                ", close=" + closeTime +
                '}';
    }
}
