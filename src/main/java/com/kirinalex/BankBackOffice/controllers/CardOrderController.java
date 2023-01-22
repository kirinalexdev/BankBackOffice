package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.dto.CardOrderDTO;
import com.kirinalex.BankBackOffice.dto.MonthlyTotalsDTO;
import com.kirinalex.BankBackOffice.dto.TopAgentsByOrdersDTO;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import com.kirinalex.BankBackOffice.utils.BadRequestException;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import com.kirinalex.BankBackOffice.utils.ErrorResponse;
import com.kirinalex.BankBackOffice.utils.ValidationMarker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.*;

@RestController
@RequestMapping(value = "/card-order", produces = "application/json") // produces для swagger
@AllArgsConstructor
@Slf4j
@Validated
@Api(value = "CardOrderController")
public class CardOrderController  {

    private final CardOrderService cardOrderService;
    private final ModelMapper employeeModelMapper;
    private final ObjectMapper objectMapper;

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    @ApiOperation(value = "Добавление заявки")
    public ResponseEntity<Object> create(@RequestBody @Valid CardOrderDTO cardOrderDTO,
                       BindingResult bindingResult) throws BadRequestException, JsonProcessingException, URISyntaxException {

        checkBindingResult(bindingResult, cardOrderDTO);

        var cardOrder = employeeModelMapper.map(cardOrderDTO, CardOrder.class);
        cardOrderService.create(cardOrder);

        return ResponseEntity
                .created(new URI("/card-order/" + cardOrder.getId()))
                .build();
    }

    @PutMapping
    @Validated(ValidationMarker.OnUpdate.class)
    @ApiOperation(value = "Изменение заявки")
    public ResponseEntity<Object> update(@RequestBody @Valid CardOrderDTO cardOrderDTO, HttpServletRequest httpRequest,
                       BindingResult bindingResult) throws BadRequestException, JsonProcessingException {

        checkBindingResult(bindingResult, cardOrderDTO);

        var id = cardOrderDTO.getId();
        var cardOrder = cardOrderService.findById(id).orElse(null);

        if (cardOrder == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        employeeModelMapper.map(cardOrderDTO, cardOrder);
        cardOrderService.update(cardOrder);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(value = "Удаление заявки")
    public ResponseEntity<Object> delete(@RequestParam int id, HttpServletRequest httpRequest) {
        var cardOrder = cardOrderService.findById(id).orElse(null);

        if (cardOrder == null) {
            return errorResponseNotFound(id, httpRequest);
        }
        cardOrderService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Получение заявки", response = CardOrderDTO.class)
    public ResponseEntity<Object> findbyid(@RequestParam int id, HttpServletRequest httpRequest) {
        var cardOrder = cardOrderService.findById(id).orElse(null);

        if (cardOrder == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        var cardOrderDTO= employeeModelMapper.map(cardOrder, CardOrderDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(cardOrderDTO);
    }

    @ApiOperation(value = "Заявки за период")
    @GetMapping("/find-by-created-on")
    public List<CardOrderDTO> findByCreatedOnBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate){
       return cardOrderService.findByCreatedOnBetweenOrderByCreatedOn(fromDate, toDate)
               .stream()
               .map(cardOrder -> employeeModelMapper.map(cardOrder, CardOrderDTO.class))
               .collect(Collectors.toList());
    }

    @ApiOperation(value = "Первые три агента с наибольшим количеством сделанных заявок")
    @GetMapping("/top-agents-by-orders-count")
    public List<TopAgentsByOrdersDTO> topAgentsByOrdersCount(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate){
       return cardOrderService.topAgentsByOrdersCount(fromDate, toDate);
    }

    @ApiOperation(value = "Помесячные итоги по заявкам")
    @GetMapping("/monthly-totals")
    public List<MonthlyTotalsDTO> monthlyTotals(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
                                                @RequestParam String currency) throws CurrencyRateException {
        return cardOrderService.monthlyTotals(fromDate, toDate, currency);
    }

    private ResponseEntity<Object> errorResponseNotFound(int idCardOrder, HttpServletRequest httpRequest){
        var status = HttpStatus.NOT_FOUND;
        var error =  new ErrorResponse(status, "Не найдена заявка с id = " + idCardOrder, httpRequest);
        return ResponseEntity.status(status).body(error);
    }

}
