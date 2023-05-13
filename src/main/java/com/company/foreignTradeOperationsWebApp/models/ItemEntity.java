package com.company.foreignTradeOperationsWebApp.models;

import com.company.foreignTradeOperationsWebApp.models.enums.TradeTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "item", schema = "foreign-trade-operations")
@NoArgsConstructor
public class ItemEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @Basic
    @Column( nullable = false, length = 45)
    private String itemName;
    @Basic
    @Column( nullable = false, precision = 0)
    private double itemCost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trade_type_id")
    private TradeTypeEntity itemType;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private transient List<OrderEntity> orders;

    public ItemEntity(String itemName, double itemCost, TradeTypeEntity itemType) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemType = itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity item = (ItemEntity) o;
        return Objects.equals(itemId, item.itemId) && Double.compare(item.itemCost, itemCost) == 0 && Objects.equals(itemName, item.itemName);
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemCost=" + itemCost +
                ", itemType='" + itemType + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, itemName, itemCost);
    }
}
