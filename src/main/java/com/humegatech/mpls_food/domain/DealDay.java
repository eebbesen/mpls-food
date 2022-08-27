package com.humegatech.mpls_food.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DealDay extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "deal_id")
    private Deal deal;
    private DayOfWeek dayOfWeek;
    private LocalDate date;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Override
    public int hashCode() {
        int result = deal != null ? deal.hashCode() : 0;
        result = 31 * result + (dayOfWeek != null ? dayOfWeek.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DealDay dealDay = (DealDay) o;

        if (deal != null ? !deal.equals(dealDay.deal) : dealDay.deal != null) return false;
        if (dayOfWeek != dealDay.dayOfWeek) return false;
        return date != null ? date.equals(dealDay.date) : dealDay.date == null;
    }
}
