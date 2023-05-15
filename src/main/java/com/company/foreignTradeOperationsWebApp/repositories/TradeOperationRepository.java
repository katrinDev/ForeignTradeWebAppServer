package com.company.foreignTradeOperationsWebApp.repositories;

import com.company.foreignTradeOperationsWebApp.models.TradeOperationEntity;
import com.company.foreignTradeOperationsWebApp.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeOperationRepository extends JpaRepository<TradeOperationEntity, Long> {
}
