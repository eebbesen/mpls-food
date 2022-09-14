package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deals")
@Entity
public class Deal extends BaseEntity {

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String cuisine;

    @Column(columnDefinition = "text")
    private String dish;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deal", orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private Set<Day> days = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deal", orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private Set<Upload> uploads = new LinkedHashSet<>();

    @Column
    private BigDecimal minPrice;

    @Column
    private BigDecimal maxPrice;

    @Column
    private BigDecimal discount;

    @Override
    public int hashCode() {
        int result = place != null ? place.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dish != null ? dish.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal deal = (Deal) o;

        if (!place.equals(deal.getPlace())) return false;
        if (!dish.equals(deal.getDish())) return false;
        return description.equals(deal.description);
    }

    @Override
    public String toString() {
        return "Deal{" +
                "description='" + description + '\'' +
                ", place=" + place.getId() +
                ", dealDays=" + days +
                ", dish=" + dish +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", discount=" + discount +
                ", dateCreated=" + getDateCreated() +
                ", lastUpdated=" + getLastUpdated() +
                '}';
    }

    public Day hasDay(final DayOfWeek dayOfWeek) {
        return days.stream()
                .filter(d -> d.getDayOfWeek() == dayOfWeek)
                .findFirst().orElse(null);
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return days.stream().map(day -> day.getDayOfWeek())
                .collect(Collectors.toList());
    }
}
