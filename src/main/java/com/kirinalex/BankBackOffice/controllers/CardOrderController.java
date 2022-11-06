package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cardorder")
@AllArgsConstructor
public class CardOrderController  {

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

    @GetMapping("/topAgentsByOrdersCount") // TODO можно ли, нужно ли тут использовать camelcase?
    public List<Map<String, Object>> topAgentsByOrdersCount(@RequestParam Date fromDate,
                                                            @RequestParam Date toDate){
       return cardOrderService.topAgentsByOrdersCount(fromDate, toDate);
    }

    @GetMapping("/monthlyTotals") // TODO можно ли, нужно ли тут использовать camelcase?
    public List<Map<String, Object>> monthlyTotals(@RequestParam Date fromDate,
                                                    @RequestParam Date toDate,
                                                    @RequestParam String currency) throws CurrencyRateException {
        //System.out.println(this.);
        return cardOrderService.monthlyTotals(fromDate, toDate, currency);
    }

//    @GetMapping("/currencyRate") // TODO можно ли, нужно ли тут использовать camelcase?
//    public double currencyRate(@RequestParam String currency){
//       return Currency.currencyRate(currency);
//    }

   // см https://sysout.ru/spring-boot-rest-api-obrabotka-isklyuchenij-chast-1/

    // TODO м.б. сделать через @ControllerAdvice
    //   про него много где пишут, например
    //   https://sysout.ru/spring-boot-rest-api-obrabotka-isklyuchenij/
    //   https://habr.com/ru/post/528116/

    // TODO предварительно проверить, есть ли

    // TODO Самому составить эту структуру или она должна сама составляться выбрасыванием исключения?
    //      Это спринг ее состаляет?
    //        {
    //        "timestamp": "2022-11-05T12:26:45.329+00:00",
    //            "status": 500,
    //            "error": "Internal Server Error",
    //            "path": "/cardorder/monthlyTotals"
    //    }

    // TODO Сделать это?
    // https://sysout.ru/spring-boot-rest-api-obrabotka-isklyuchenij-chast-1/
    // server.error.include-message=always
}
