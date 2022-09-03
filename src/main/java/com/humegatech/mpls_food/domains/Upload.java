package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "uploads")
public class Upload extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "deal_id", nullable = false)
    @JsonBackReference
    private Deal deal;

    @Lob
    private byte[] image;

    @Column
    private boolean verified;

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