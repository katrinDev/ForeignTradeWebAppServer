package com.company.foreignTradeOperationsWebApp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "trade_operation", schema = "foreign-trade-operations")
@NoArgsConstructor
public class TradeOperationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "operation_id", nullable = false)
    private Long operationId;

    @Basic
    @Column( nullable = true, precision = 0)
    private double fullCost;

    @Basic
    @Column( nullable = false)
    private String supplyDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="company_id")
    private CompanyEntity company;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private transient List<OrderEntity> orders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trade_type_id", nullable = false)
    private TradeTypeEntity tradeType;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "operations_users",
            joinColumns = { @JoinColumn(name = "operation_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )

    private Set<UserEntity> users = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeOperationEntity that = (TradeOperationEntity) o;
        return Objects.equals(operationId, that.operationId) && Objects.equals(tradeType, that.tradeType) && Objects.equals(fullCost, that.fullCost) && Objects.equals(supplyDate, that.supplyDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, tradeType, fullCost, supplyDate);
    }

    @Override
    public String toString() {
        return "TradeOperation{" +
                "operationId=" + operationId +
                ", operationType='" + tradeType + '\'' +
                ", fullCost=" + fullCost +
                ", supplyDate=" + supplyDate +
                ", company=" + company +
                ", orders=" + orders +
                ", users=" + users +
                '}';
    }
}
