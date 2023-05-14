package com.company.foreignTradeOperationsWebApp.controllers;

import com.company.foreignTradeOperationsWebApp.models.ItemEntity;
import com.company.foreignTradeOperationsWebApp.models.TradeTypeEntity;
import com.company.foreignTradeOperationsWebApp.payloads.response.MessageResponse;
import com.company.foreignTradeOperationsWebApp.repositories.ItemRepository;
import com.company.foreignTradeOperationsWebApp.repositories.TrendTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/items")
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TrendTypeRepository trendTypeRepository;

    @GetMapping("")
    public ResponseEntity<List<ItemEntity>> getAllItems(){
        List<ItemEntity> items = itemRepository.findAll();

        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<ItemEntity> deleteItem(@PathVariable("id") Long id){
        ItemEntity item = itemRepository.findById(id).orElse(null);

        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        itemRepository.deleteById(id);
        System.out.println("Item to be deleted: " + item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addItem(@RequestBody ItemEntity item) {
        if (item == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        System.out.println("New item: " + item);

        if (itemRepository.existsByItemName(item.getItemName())) {
            return new ResponseEntity<>(new MessageResponse("Ошибка: Такой товар уже существует!"), HttpStatus.BAD_REQUEST);
        }

        TradeTypeEntity tradeTypeEntity = trendTypeRepository.findByTradeTypeName(item.getTradeType().getTradeTypeName());
        ItemEntity newItem = new ItemEntity(item.getItemName(), item.getItemCost(), tradeTypeEntity);
        itemRepository.save(newItem);

        return ResponseEntity.ok(newItem);
    }

    @PutMapping (value = "")
    public ResponseEntity<?> updateItem(@RequestBody ItemEntity item) {

        System.out.println("body" + item);
        if (item == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        try{
            if(itemRepository.findById(item.getItemId()).orElse(null) == null){
                return new ResponseEntity<>(new MessageResponse("Данного товара нет в системе!"), HttpStatus.NOT_FOUND);
            }

            if (itemRepository.existsByItemName(item.getItemName()) &&
                    !Objects.equals((itemRepository.findByItemName(item.getItemName())).getItemId(), item.getItemId())) {
                return new ResponseEntity<>(new MessageResponse("Ошибка: Товар с данным именем уже существует!"), HttpStatus.BAD_REQUEST);
            }

            TradeTypeEntity tradeTypeEntity = trendTypeRepository.findByTradeTypeName(item.getTradeType().getTradeTypeName());
            ItemEntity updatedItem = new ItemEntity(item.getItemId(), item.getItemName(), item.getItemCost(), tradeTypeEntity);
            itemRepository.save(updatedItem);
            System.out.println("Item was updated: " + updatedItem);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new MessageResponse("Не удалось изменить данные товара!"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

}
