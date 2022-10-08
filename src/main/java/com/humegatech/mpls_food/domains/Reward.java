package com.humegatech.mpls_food.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "rewards")
public class Reward extends BaseEntity {
    @Column
    private String notes;

    @Column
    private RewardType rewardType;

    @OneToOne
    @MapsId
    @JoinColumn(name = "place_id")
    private Place place;

    @Override
    public int hashCode() {
        int result = notes != null ? notes.hashCode() : 0;
        result = 31 * result + rewardType.hashCode();
        result = 31 * result + place.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reward reward = (Reward) o;

        if (!Objects.equals(notes, reward.notes)) return false;
        if (rewardType != reward.rewardType) return false;
        return place.equals(reward.place);
    }

    @Override
    public String toString() {
        return "Reward{" +
                "notes='" + notes + '\'' +
                ", rewardType=" + rewardType +
                ", place=" + place +
                '}';
    }
}
