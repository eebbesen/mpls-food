package com.humegatech.mpls_food.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Place extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    @Builder.Default
    private Set<Deal> placeDeals = new HashSet<>();

    @Column
    private String website;

    @Column
    @Value("false")
    private boolean app;

    @Column
    @Value("false")
    private boolean orderAhead;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + (placeDeals != null ? placeDeals.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (app ? 1 : 0);
        result = 31 * result + (orderAhead ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (app != place.app) return false;
        if (orderAhead != place.orderAhead) return false;
        if (!name.equals(place.name)) return false;
        if (!address.equals(place.address)) return false;
        if (placeDeals != null ? !placeDeals.equals(place.placeDeals) : place.placeDeals != null) return false;
        return website != null ? website.equals(place.website) : place.website == null;
    }
}
