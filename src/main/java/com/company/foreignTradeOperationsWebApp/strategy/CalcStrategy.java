package com.company.foreignTradeOperationsWebApp.strategy;

import com.company.foreignTradeOperationsWebApp.models.OrderEntity;

import java.util.List;

public interface CalcStrategy {
    double calculate(List<OrderEntity> orders);

}
