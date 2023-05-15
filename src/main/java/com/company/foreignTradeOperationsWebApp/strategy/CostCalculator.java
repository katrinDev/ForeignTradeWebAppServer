package com.company.foreignTradeOperationsWebApp.strategy;

import com.company.foreignTradeOperationsWebApp.models.OrderEntity;

import java.util.List;

public class CostCalculator {

    CalcStrategy calcStrategy;


    public CostCalculator(CalcStrategy calcStrategy) {
        this.calcStrategy = calcStrategy;
    }

    public double countCost(List<OrderEntity> orders){
        return calcStrategy.calculate(orders);
    }

    public void setCalcStrategy(CalcStrategy calcStrategy) {
        this.calcStrategy = calcStrategy;
    }
}
