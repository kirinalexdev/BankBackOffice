package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cardorder")
@AllArgsConstructor
public class CardOrderController {

    private final CardOrderService cardOrderService;

    @PostMapping("/create")
    public void create(@RequestBody CardOrder cardOrder){
        cardOrderService.create(cardOrder);
        System.out.println(cardOrder);
    }

    @GetMapping("/findbyid")
    public CardOrder findbyid(@RequestParam int id){
        return cardOrderService.findById(id);
    }

    // TODO сделать чтобы возвращал текстовую плоскую инфу, без id, без вложенностей
    @GetMapping("/findbycreatedon") // TODO можно ли, нужно ли тут использовать camelcase?
    public List<CardOrder> findByCreatedOnBetween(@RequestParam Date fromDate,
                                                  @RequestParam Date toDate){
       return cardOrderService.findByCreatedOnBetween(fromDate, toDate);
    }
}
