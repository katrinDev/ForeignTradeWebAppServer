package com.company.foreignTradeOperationsWebApp.repositories;

import com.company.foreignTradeOperationsWebApp.models.TradeTypeEntity;
import com.company.foreignTradeOperationsWebApp.models.enums.TradeTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendTypeRepository extends JpaRepository<TradeTypeEntity, Long> {
    TradeTypeEntity findByTradeTypeName(TradeTypeEnum tradeTypeName);
}
