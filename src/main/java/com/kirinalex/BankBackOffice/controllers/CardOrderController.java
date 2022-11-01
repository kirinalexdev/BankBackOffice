package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.models.CardOrder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cardorder")
@AllArgsConstructor
public class CardOrderController {

    @PostMapping("/create")
    // TODO добавить другие параметры к @RequestParam?
    // TODO может быть правильнее использовать @RequestBody?
    // TODO добавить @Valid, BindingResult и прочее
    public void create(@RequestBody CardOrder cardOrder){
        System.out.println(cardOrder);
        // TODO возвращать тут ок неок?
    }

    // TODO сделать все действия с заказом: обновление, удаление, получение,..

}
