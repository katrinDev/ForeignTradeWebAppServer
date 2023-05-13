package com.company.foreignTradeOperationsWebApp.models;

import com.company.foreignTradeOperationsWebApp.models.enums.TradeTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trade_type", schema = "foreign-trade-operations")
@ToString
public class TradeTypeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "trade_type_id", nullable = false)
    private Long tradeTypeId;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false, length = 45)
    private TradeTypeEnum tradeTypeName;

    @OneToMany (mappedBy = "tradeType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private transient List<CompanyEntity> companies;

    @OneToMany (mappedBy = "tradeType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private transient List<ItemEntity> items;

    @OneToMany (mappedBy = "tradeType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private transient List<TradeOperationEntity> tradeOperations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeTypeEntity that = (TradeTypeEntity) o;
        return Objects.equals(tradeTypeId, that.tradeTypeId) && tradeTypeName == that.tradeTypeName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeTypeId, tradeTypeName);
    }
}
