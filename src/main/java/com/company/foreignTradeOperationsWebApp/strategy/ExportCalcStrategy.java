package com.company.foreignTradeOperationsWebApp.strategy;



import com.company.foreignTradeOperationsWebApp.models.OrderEntity;

import java.util.List;

public class ExportCalcStrategy implements CalcStrategy{
    private double cost;

    @Override
    public double calculate(List<OrderEntity> orders){
        if(cost == 0.00){
            for(OrderEntity i : orders){
                cost += i.getItem().getItemCost() * i.getItemsAmount();
            }
        }

        return cost;
    }

}
