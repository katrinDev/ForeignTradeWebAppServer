package com.company.foreignTradeOperationsWebApp.strategy;

import com.company.foreignTradeOperationsWebApp.models.OrderEntity;

import java.util.List;

public class ImportCalcStrategy implements CalcStrategy{
    private double cost;
    private static final int VAT = 20;

    @Override
    public double calculate(List<OrderEntity> orders){
        if(cost == 0.00){
            for(OrderEntity i : orders){
                cost += i.getItem().getItemCost() * i.getItemsAmount();
            }
            cost += cost * VAT * 0.01;
        }

        return cost;
    }
}

