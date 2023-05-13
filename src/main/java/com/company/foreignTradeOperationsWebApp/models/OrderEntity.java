package com.company.foreignTradeOperationsWebApp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "order_", schema = "foreign-trade-operations")
@NoArgsConstructor
public class OrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column( nullable = false)
    private Long orderId;

    @Basic
    @Column( nullable = false)
    private int itemsAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item_id")
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="operation_id")
    private TradeOperationEntity operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity order = (OrderEntity) o;
        return Objects.equals(orderId, order.orderId) &&  itemsAmount == order.itemsAmount && Objects.equals(item, order.item) && Objects.equals(operation, order.operation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, itemsAmount, item, operation);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", itemAmount=" + itemsAmount +
                ", item=" + item +
                ", operation=" + operation +
                '}';
    }
}
