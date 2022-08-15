package com.humegatech.mpls_food.domain;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deal extends BaseEntity {

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @ManyToOne
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

    @Override
    public int hashCode() {
        // TODO: why is description ever null when I don't allow that
        int result = description == null ? 31 : description.hashCode();
        result = 31 * result + (sunday ? 1 : 0);
        result = 31 * result + (monday ? 1 : 0);
        result = 31 * result + (tuesday ? 1 : 0);
        result = 31 * result + (wednesday ? 1 : 0);
        result = 31 * result + (thursday ? 1 : 0);
        result = 31 * result + (friday ? 1 : 0);
        result = 31 * result + (saturday ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal deal = (Deal) o;

        if (sunday != deal.sunday) return false;
        if (monday != deal.monday) return false;
        if (tuesday != deal.tuesday) return false;
        if (wednesday != deal.wednesday) return false;
        if (thursday != deal.thursday) return false;
        if (friday != deal.friday) return false;
        if (saturday != deal.saturday) return false;
        return (description != null ? description.equals(deal.description) : deal.description != null);
    }
}
