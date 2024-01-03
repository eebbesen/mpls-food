package com.humegatech.mpls_food.domains;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deals")
@Entity
public class Deal extends BaseEntity {

    @Column
    boolean taxIncluded;
    @Column
    DealType dealType;
    @Column(nullable = false, columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String dish;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "place_id", foreignKey = @ForeignKey(name = "fk_deals_places"))
    private Place place;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deal", orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private Set<Day> days = new LinkedHashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deal", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private Set<Upload> uploads = new LinkedHashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deal", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private Set<DealLog> dealLogs = new LinkedHashSet<>();
    @Column
    private Double minPrice;
    @Column
    private Double maxPrice;
    @Column
    private Double minDiscount;
    @Column
    private Double maxDiscount;
    @Column
    private Double minDiscountPercent;
    @Column
    private Double maxDiscountPercent;
    @Column
    private boolean verified;
    @Column
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @Column
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

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
        if (!(o instanceof Deal deal)) return false;

        if (!place.equals(deal.getPlace())) return false;
        if (!dish.equals(deal.getDish())) return false;
        return description.equals(deal.description);
    }

    @Override
    public String toString() {
        return "Deal{" +
                "taxIncluded=" + taxIncluded +
                ", description=" + description +
                ", dish=" + dish +
                ", place=" + place +
                ", days=" + days +
                ", uploads=" + uploads +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minDiscount=" + minDiscount +
                ", maxDiscount=" + maxDiscount +
                ", minDiscountPercent=" + minDiscountPercent +
                ", maxDiscountPercent=" + maxDiscountPercent +
                ", verified=" + verified +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", dealType=" + dealType +
                '}';
    }

    public Day hasDay(final DayOfWeek dayOfWeek) {
        return days.stream()
                .filter(d -> d.getDayOfWeek() == dayOfWeek)
                .findFirst().orElse(null);
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return days.stream().map(Day::getDayOfWeek)
                .collect(Collectors.toList());
    }
}
