package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "days")
@EntityListeners(AuditingEntityListener.class)
public class Day extends BaseEntity {
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "deal_id", foreignKey = @ForeignKey(name = "fk_days_deals"))
    private Deal deal;
    private DayOfWeek dayOfWeek;
    private LocalDate date;

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
        if (o == null || !(o instanceof Day)) return false;

        Day day = (Day) o;

        if (deal != null ? !deal.equals(day.deal) : day.deal != null) return false;
        if (dayOfWeek != day.dayOfWeek) return false;
        return date != null ? date.equals(day.date) : day.date == null;
    }

    @Override
    public String toString() {
        return "Day{" +
                "deal=" + deal.getId() +
                ", dayOfWeek=" + dayOfWeek +
                ", date=" + date +
                ", dateCreated=" + getDateCreated() +
                ", lastUpdated=" + getLastUpdated() +
                '}';
    }
}
