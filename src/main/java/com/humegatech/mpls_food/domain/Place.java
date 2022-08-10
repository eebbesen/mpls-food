package com.humegatech.mpls_food.domain;

import java.time.OffsetDateTime;
import java.util.Set;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Place {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @OneToMany
    @JoinColumn(name = "place_id")
    private Set<Deal> placeDeals;

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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }

    public boolean isApp() { return app; }

    public void setApp(boolean app) { this.app = app; }

    public boolean isOrderAhead() { return orderAhead; }

    public void setOrderAhead(boolean orderAhead) { this.orderAhead = orderAhead; }

    public Set<Deal> getPlaceDeals() {
        return placeDeals;
    }

    public void setPlaceDeals(final Set<Deal> placeDeals) {
        this.placeDeals = placeDeals;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
