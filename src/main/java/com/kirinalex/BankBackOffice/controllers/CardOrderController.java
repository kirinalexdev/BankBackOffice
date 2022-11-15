package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.dto.CardOrderDTO;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.repositories.CardOrderRepository;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import com.kirinalex.BankBackOffice.utils.BadRequestException;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import com.kirinalex.BankBackOffice.utils.ErrorResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.generateErrorMessage;

@RestController
@RequestMapping("/card-order")
@AllArgsConstructor
public class CardOrderController  {

    private final CardOrderService cardOrderService;
    private final ModelMapper employeeModelMapper;
    private final ModelMapper employeeModelMapper2;

    @PostMapping
    public void create(@RequestBody @Valid CardOrderDTO cardOrderDTO,
                       BindingResult bindingResult) throws BadRequestException {
        // TODO проверять, что ID не передан
        if (bindingResult.hasErrors()) {
            var s = generateErrorMessage(bindingResult.getFieldErrors());
            throw new BadRequestException(s);
        }

        var cardOrder = employeeModelMapper.map(cardOrderDTO, CardOrder.class);
        cardOrderService.create(cardOrder);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody @Valid CardOrderDTO cardOrderDTO, HttpServletRequest httpRequest,
                       BindingResult bindingResult) throws BadRequestException {

        if (bindingResult.hasErrors()) {
            var s = generateErrorMessage(bindingResult.getFieldErrors());
            throw new BadRequestException(s);
        }

        // TODO сделать modelMapper отдельным классом как тут? https://tproger.ru/articles/chto-takoe-modelmapper-i-zachem-on-nuzhen/
        //      назвать как то по привязанно к cardOrder, а не просто modelMapper
        var id = cardOrderDTO.getId(); // TODO удалить, дублирует
        var cardOrder = cardOrderService.findById(id).orElse(null);

        if (cardOrder == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        employeeModelMapper.map(cardOrderDTO, cardOrder);
        cardOrderService.update(cardOrder);

        return new ResponseEntity<>(null, HttpStatus.OK);
        // TODO остальные методы тоже маппить
    }

    @DeleteMapping
    public void delete(@RequestParam int id) {
        cardOrderService.delete(id);
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<Object> findbyid(@RequestParam int id, HttpServletRequest httpRequest) {
        var cardOrder = cardOrderService.findById(id).orElse(null);

        if (cardOrder == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        var cardOrderDTO= employeeModelMapper.map(cardOrder, CardOrderDTO.class);
        return new ResponseEntity<>(cardOrderDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-created-on")
    public List<CardOrderDTO> findByCreatedOnBetween(@RequestParam Date fromDate,
                                                  @RequestParam Date toDate){
       return cardOrderService.findByCreatedOnBetween(fromDate, toDate)
               .stream()
               .map(cardOrder -> employeeModelMapper.map(cardOrder, CardOrderDTO.class))
               .collect(Collectors.toList());
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

    private ResponseEntity<Object> errorResponseNotFound(int idCardOrder, HttpServletRequest httpRequest){
        var status = HttpStatus.NOT_FOUND;
        var error =  new ErrorResponse(status, "Не найдена заявка с id = " + idCardOrder, httpRequest);
        return new ResponseEntity<>(error, status);
    }

}
