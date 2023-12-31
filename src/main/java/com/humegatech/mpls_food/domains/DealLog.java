package com.humegatech.mpls_food.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deal_logs")
@Entity
public class DealLog extends BaseEntity {

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column
    private DealType dealType;

    @Column
    private Boolean redeemed;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate redemptionDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "place_id", foreignKey = @ForeignKey(name = "fk_deal_log_places"))
    private Place place;

    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "deal_id", foreignKey = @ForeignKey(name = "fk_deal_log_deals"))
    private Deal deal;

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + dealType.hashCode();
        result = 31 * result + (redeemed != null ? redeemed.hashCode() : 0);
        result = 31 * result + (redemptionDate != null ? redemptionDate.hashCode() : 0);
        result = 31 * result + place.hashCode();
        result = 31 * result + (deal != null ? deal.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DealLog dealLog = (DealLog) o;

        if (!description.equals(dealLog.description)) return false;
        if (dealType != dealLog.dealType) return false;
        if (!Objects.equals(redeemed, dealLog.redeemed)) return false;
        if (!Objects.equals(redemptionDate, dealLog.redemptionDate))
            return false;
        if (!place.equals(dealLog.place)) return false;
        return Objects.equals(deal, dealLog.deal);
    }

    @Override
    public String toString() {
        return "DealLog{" +
                "description='" + description + '\'' +
                ", dealType=" + dealType +
                ", redeemed=" + redeemed +
                ", redemptionDate=" + redemptionDate +
                ", place=" + place.getId() +
                ", deal=" + (null != deal ? deal.getId() : null) +
                '}';
    }
}
