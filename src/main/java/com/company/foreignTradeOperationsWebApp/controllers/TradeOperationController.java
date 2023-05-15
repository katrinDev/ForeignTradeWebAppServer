package com.company.foreignTradeOperationsWebApp.controllers;

import com.company.foreignTradeOperationsWebApp.models.*;
import com.company.foreignTradeOperationsWebApp.payloads.response.MessageResponse;
import com.company.foreignTradeOperationsWebApp.repositories.*;
import com.company.foreignTradeOperationsWebApp.strategy.CalcStrategy;
import com.company.foreignTradeOperationsWebApp.strategy.CostCalculator;
import com.company.foreignTradeOperationsWebApp.strategy.ExportCalcStrategy;
import com.company.foreignTradeOperationsWebApp.strategy.ImportCalcStrategy;
import jakarta.persistence.criteria.Order;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trades")
public class TradeOperationController {
    @Autowired
    TradeOperationRepository tradeOperationRepository;

    @Autowired
    TrendTypeRepository trendTypeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("")
    public ResponseEntity<List<TradeOperationEntity>> getAllTrades(){
        List<TradeOperationEntity> trades = tradeOperationRepository.findAll();

        if (trades.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getMyTrades(@PathVariable("id") Long id){
        UserEntity user = userRepository.findByUserId(id).orElse(null);

        System.out.println(user);
        if(user != null) {
            List<TradeOperationEntity> trades = tradeOperationRepository.findAll().
                    stream().filter(trade -> trade.getUsers().contains(user)).toList();

            if (trades.isEmpty()) {
                return new ResponseEntity<>(new MessageResponse("Сделки для данного пользователя не найдены!"), HttpStatus.NOT_FOUND);
            }
            System.out.println(trades);
            return new ResponseEntity<>(trades, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Данного пользователя нет в системе!"), HttpStatus.NOT_FOUND);
        }
    }


    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<TradeOperationEntity> deleteTrade(@PathVariable("id") Long id){
        TradeOperationEntity trade = tradeOperationRepository.findById(id).orElse(null);

        if (trade == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tradeOperationRepository.deleteById(id);
        System.out.println("Trade operation to be deleted: " + trade);
        return new ResponseEntity<>(trade, HttpStatus.OK);
    }


    @PostMapping("{id}")
    public ResponseEntity<?> addTrade(@RequestBody TradeOperationEntity tradeOperation, @PathVariable("id") Long id) {
        if (tradeOperation == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        System.out.println("New trade: " + tradeOperation);

        try{

            TradeTypeEntity tradeTypeEntity = trendTypeRepository.findByTradeTypeId(tradeOperation.getTradeType().getTradeTypeId());

            CompanyEntity company = companyRepository.findByCompanyName(tradeOperation.getCompany().getCompanyName());
            TradeOperationEntity newTradeOperation = new TradeOperationEntity(tradeOperation.getSupplyDate(), company, tradeTypeEntity);


            UserEntity user = userRepository.findByUserId(id).orElse(null);

            System.out.println(user);
            if(user == null) {
                return new ResponseEntity<>(new MessageResponse("Ошибка: Данного пользователя нет в системе"), HttpStatus.BAD_REQUEST);
            } else {
                newTradeOperation.addUser(user);
            }
            TradeOperationEntity addedTrade = tradeOperationRepository.save(newTradeOperation);
            System.out.println("New trade was added: " + addedTrade);


            List<OrderEntity> orderEntities =  tradeOperation.getOrders().stream().map(order -> {
                ItemEntity item = itemRepository.findByItemName(order.getItem().getItemName());
                return new OrderEntity(order.getItemsAmount(), item);
            }).toList();

            orderRepository.saveAll(orderEntities);

            CalcStrategy calcStrategy = null;

            if(addedTrade.getTradeType().getTradeTypeId() == 2){
                calcStrategy = new ExportCalcStrategy();
            } else if (addedTrade.getTradeType().getTradeTypeId() == 1){
                calcStrategy = new ImportCalcStrategy();
            }

           // List<OrderEntity> orders = tradeOperationRepository.findById(addedTrade.getOperationId()).get().getOrders();
            CostCalculator costCalculator = new CostCalculator(calcStrategy);
            addedTrade.setFullCost(costCalculator.countCost(orderEntities));

            newTradeOperation = tradeOperationRepository.save(addedTrade);

            return ResponseEntity.ok(newTradeOperation);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new MessageResponse("Ошибка: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
