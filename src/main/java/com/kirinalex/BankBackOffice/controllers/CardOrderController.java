package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import com.kirinalex.BankBackOffice.utils.BadRequestException;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.generateErrorMessage;

@RestController
@RequestMapping("/card-order")
@AllArgsConstructor
public class CardOrderController  {

    private final CardOrderService cardOrderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED) // TODO может быть тут другой статус вернуть. ведь тут помещение в кафку, не создание
    public void create(@RequestBody @Valid CardOrder cardOrder,
                       BindingResult bindingResult) throws BadRequestException {
    // TODO гуглить как избавиться от ошибки Unhandled exception, при отсутствии throws BadRequestException
    //      ведь у меня есть @ControllerAdvice

        if (bindingResult.hasErrors()) {
            String s = generateErrorMessage(bindingResult.getFieldErrors());
            throw new BadRequestException(s);
        }

        cardOrderService.create(cardOrder);
        System.out.println(cardOrder); // TODO отладка
    }

    // TODO в чем отличие создания от редактирования?
//    @PostMapping("/edit")
//    public void edit(@RequestBody @Valid CardOrder cardOrder,
//                       BindingResult bindingResult) throws BadRequestException {
//
//        if (bindingResult.hasErrors()) {
//            String s = generateErrorMessage(bindingResult.getFieldErrors());
//            throw new BadRequestException(s);
//        }
//
//        cardOrderService.create(cardOrder);
//        System.out.println(cardOrder); // TODO отладка
//    }

    @GetMapping("/find-by-id")
    public CardOrder findbyid(@RequestParam int id){
        return cardOrderService.findById(id);
    }

    // TODO сделать чтобы возвращал текстовую плоскую инфу, без id, без вложенностей?
    @GetMapping("/find-by-created-on")
    public List<CardOrder> findByCreatedOnBetween(@RequestParam Date fromDate,
                                                  @RequestParam Date toDate){
       return cardOrderService.findByCreatedOnBetween(fromDate, toDate);
    }

    @GetMapping("/top-agents-by-orders-count")
    public List<Map<String, Object>> topAgentsByOrdersCount(@RequestParam Date fromDate,
                                                            @RequestParam Date toDate){
       return cardOrderService.topAgentsByOrdersCount(fromDate, toDate);
    }

    @GetMapping("/monthly-totals")
    public List<Map<String, Object>> monthlyTotals(@RequestParam Date fromDate,
                                                    @RequestParam Date toDate,
                                                    @RequestParam String currency) throws CurrencyRateException {
        return cardOrderService.monthlyTotals(fromDate, toDate, currency);
    }


}
