package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
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


    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private Set<Upload> uploads = new LinkedHashSet<>();

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
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
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
