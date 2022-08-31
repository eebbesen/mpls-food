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

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deal", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private Set<Day> days = new LinkedHashSet<>();

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal deal = (Deal) o;

        return description != null ? !description.equals(deal.description) : deal.description != null;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "description='" + description + '\'' +
                ", place=" + place.getId() +
                ", dealDays=" + days +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                '}';
    }


    public Day hasDay(final DayOfWeek dayOfWeek) {
        for (Day day : days) {
            if (day.getDayOfWeek() == dayOfWeek) {
                return day;
            }
        }

        return null;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return days.stream().map(day -> day.getDayOfWeek())
                .collect(Collectors.toList());
    }
}
