package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

    @Column
    boolean taxIncluded;
    @Column(nullable = false, columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String cuisine;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deal", orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private Set<Upload> uploads = new LinkedHashSet<>();
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
    private String startTime;
    @Column
    private String endTime;
    @Column
    private LocalDate startDate;
    @Column
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
        if (o == null || !(o instanceof Deal)) return false;

        Deal deal = (Deal) o;

        if (!place.equals(deal.getPlace())) return false;
        if (!dish.equals(deal.getDish())) return false;
        return description.equals(deal.description);
    }

    @Override
    public String toString() {
        return "Deal{" +
                "taxIncluded=" + taxIncluded +
                ", description='" + description + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", dish='" + dish + '\'' +
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
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
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
