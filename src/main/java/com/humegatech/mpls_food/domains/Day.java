package com.humegatech.mpls_food.domains;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
        if (!(o instanceof Day day)) return false;

        if (!Objects.equals(deal, day.deal)) return false;
        if (dayOfWeek != day.dayOfWeek) return false;
        return Objects.equals(date, day.date);
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
