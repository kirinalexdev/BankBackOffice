package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public CardOrder get(@RequestParam int id){
        return cardOrderService.findById(id);
    }

}
