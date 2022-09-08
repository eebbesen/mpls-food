package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Arrays;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "uploads")
public class Upload extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "deal_id", nullable = false)
    @JsonBackReference
    private Deal deal;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;

    @Column
    private boolean verified;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @Override
    public int hashCode() {
        int result = deal.hashCode();
        result = 31 * result + Arrays.hashCode(image);
        result = 31 * result + (verified ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Upload upload = (Upload) o;

        if (verified != upload.verified) return false;
        if (!deal.equals(upload.deal)) return false;
        return Arrays.equals(image, upload.image);
    }

    @Override
    public String toString() {
        return "Upload{" +
                "deal=" + deal.getId() +
                ", image=" + Arrays.toString(image) +
                ", verified=" + verified +
                '}';
    }
}