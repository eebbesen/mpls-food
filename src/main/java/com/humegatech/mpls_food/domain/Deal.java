package com.humegatech.mpls_food.domain;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Deal {

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

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Column
    @Value("false")
    private boolean sunday;

    @Column
    @Value("false")
    private boolean monday;

    @Column
    @Value("false")
    private boolean tuesday;

    @Column
    @Value("false")
    private boolean wednesday;

    @Column
    @Value("false")
    private boolean thursday;

    @Column
    @Value("false")
    private boolean friday;

    @Column
    @Value("false")
    private boolean saturday;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(final Place place) {
        this.place = place;
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

    public boolean isSunday() { return sunday; }

    public void setSunday(final boolean sunday) { this.sunday = sunday; }

    public boolean isMonday() { return monday; }

    public void setMonday(final boolean monday) { this.monday = monday; }

    public boolean isTuesday() { return tuesday; }

    public void setTuesday(final boolean tuesday) { this.tuesday = tuesday; }

    public boolean isWednesday() { return wednesday; }

    public void setWednesday(final boolean wednesday) { this.wednesday = wednesday; }

    public boolean isThursday() { return thursday; }

    public void setThursday(final boolean thursday) { this.thursday = thursday; }

    public boolean isFriday() { return friday; }

    public void setFriday(final boolean friday) { this.friday = friday; }

    public boolean isSaturday() { return saturday; }

    public void setSaturday(final boolean saturday) { this.saturday = saturday; }

}
