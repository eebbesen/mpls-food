package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Column
    private String website;

    @Column
    @Value("false")
    private boolean app;

    @Column
    @Value("false")
    private boolean orderAhead;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "place", fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Reward reward;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "place", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private Set<DealLog> DealLogs = new LinkedHashSet<>();

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
        if (o == null || !(o instanceof Place)) return false;

        Place place = (Place) o;

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
