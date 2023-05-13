package com.company.foreignTradeOperationsWebApp.repositories;

import com.company.foreignTradeOperationsWebApp.models.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    boolean existsByItemName(String name);

    ItemEntity findByItemName(String name);
}
