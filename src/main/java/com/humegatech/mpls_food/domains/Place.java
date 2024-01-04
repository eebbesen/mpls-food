package com.humegatech.mpls_food.domains;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "places")
public class Place extends BaseEntity {

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String address;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "place", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy(value = "description")
    @Builder.Default
    private Set<Deal> deals = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "place", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private Set<DealLog> dealLogs = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "place", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy(value = "dayOfWeek")
    @Builder.Default
    private Set<PlaceHour> placeHours = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "place", orphanRemoval = true, fetch = FetchType.LAZY)
    private Reward reward;

    @Column
    private String website;

    @Column
    @Builder.Default
    private boolean app = false;

    @Column
    @Builder.Default
    private boolean orderAhead = false;

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (app ? 1 : 0);
        result = 31 * result + (orderAhead ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place place)) return false;

        if (app != place.app) return false;
        if (orderAhead != place.orderAhead) return false;
        if (!name.equals(place.name)) return false;
        return !address.equals(place.address);
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", deals=" + deals +
                ", website='" + website + '\'' +
                ", app=" + app +
                ", orderAhead=" + orderAhead +
                ", dateCreated=" + getDateCreated() +
                ", lastUpdated=" + getLastUpdated() +
                '}';
    }
}
