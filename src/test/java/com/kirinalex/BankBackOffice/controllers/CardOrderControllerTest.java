package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.dto.CardOrderDTO;
import com.kirinalex.BankBackOffice.dto.EmployeeDTOId;
import com.kirinalex.BankBackOffice.dto.MonthlyTotalsDTO;
import com.kirinalex.BankBackOffice.dto.TopAgentsByOrdersDTO;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CardOrderController.class)
@ActiveProfiles("test")                   // отключаем security
@AutoConfigureMockMvc(addFilters = false) //
class CardOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardOrderService cardOrderService;

    @SpyBean
    private ModelMapper employeeModelMapper;


    @SneakyThrows
    @Test
    public void givenCardOrderDTO_whenCreateCardOrder_thenReturnIsCreated(){
        // given
        var cardOrderDTO = cardOrderDTO();

        // when
        var response = mockMvc.perform(post("/card-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardOrderDTO)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", matchesPattern("\\/card-order\\/\\d+")));
    }

    @SneakyThrows
    @Test
    public void givenIncorrectCardOrderDTO_whenCreateCardOrder_thenReturnBadRequest(){
        // given
        var cardOrderDTO = incorrectCardOrderDTO();

        // when
        var response = mockMvc.perform(post("/card-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardOrderDTO)));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("agent")))
                .andExpect(jsonPath("$.message", containsString("creditLimit")))
                .andExpect(jsonPath("$.message", containsString("client")));
    }

    @SneakyThrows
    @Test
    public void givenCardOrderDTO_whenUpdateCardOrder_thenReturnOk(){
        // given
        var cardOrderDTO = cardOrderDTO();
        given(cardOrderService.findById(anyInt()))
                .willReturn(Optional.of(new CardOrder()));

        // when
        var response = mockMvc.perform(put("/card-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardOrderDTO)));

        // then
        response.andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void givenCardOrderDTO_whenUpdateNotExistCardOrder_thenReturnNotFound(){
        // given
        var cardOrderDTO = cardOrderDTO();
        given(cardOrderService.findById(anyInt()))
                .willReturn(Optional.empty());

        // when
        var response = mockMvc.perform(put("/card-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardOrderDTO)));

        // then
        response.andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void givenCardOrderId_whenDeleteCardOrder_thenReturnOk(){
        // given
        given(cardOrderService.findById(anyInt()))
                .willReturn(Optional.of(new CardOrder()));

        // when
        var response = mockMvc.perform(
                delete("/card-order")
                        .param("id", "2"));

        // then
        response.andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void givenCardOrderId_whenDeleteNotExistCardOrder_thenReturnNotFound(){
        // given
        given(cardOrderService.findById(anyInt()))
                .willReturn(Optional.empty());

        // when
        var response = mockMvc.perform(
                delete("/card-order")
                        .param("id", "2"));

        // then
        response.andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void givenEmployeeId_whenFindEmployee_thenReturnEmployeeDTO(){
        // given
        var id = 1;
        var agentId = 10;
        var creditLimit = new BigDecimal(100);
        var client = "Client1";

        var cardOrder= CardOrder.builder()
                .id(id)
                .agent(Employee.builder().id(agentId).build())
                .creditLimit(creditLimit)
                .client(client)
                .build();

        var expectedCardOrderDTO = CardOrderDTO.builder()
                .id(id)
                .agent(EmployeeDTOId.builder().id(agentId).build())
                .creditLimit(creditLimit)
                .client(client)
                .build();

        given(cardOrderService.findById(id))
                .willReturn(Optional.of(cardOrder));

        // when
        var resultActions = mockMvc.perform(
                get("/card-order")
                        .param("id", String.valueOf(id)));

        var result = resultActions.andReturn();

        // then
        resultActions.andExpect(status().isOk());

        CardOrderDTO actualCardOrderDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<CardOrderDTO>() {});

        assertThat(actualCardOrderDTO).isEqualTo(expectedCardOrderDTO);
    }


    @SneakyThrows
    @Test
    public void givenCardOrderId_whenFindNotExistCardOrder_thenReturnNotFound(){
        // given
        var cardOrderId = 2;

        given(cardOrderService.findById(cardOrderId))
                .willReturn(Optional.empty());

        // when
        var response = mockMvc.perform(
                get("/card-order")
                        .param("id", String.valueOf(cardOrderId)));

        // then
        response.andExpect(status().isNotFound());
    }
    @SneakyThrows
    @Test
    public void givenPeriod_whenFindCardOrder_thenReturnListCardOrderDTO(){
        // given
        var id1 = 1;
        var id2 = 2;
        var agentId1 = 10;
        var agentId2 = 20;
        var creditLimit1 = new BigDecimal(100);
        var creditLimit2 = new BigDecimal(200);
        var client1 = "Client1";
        var client2 = "Client2";

        var originalList = new ArrayList<CardOrder>();
        originalList.add(CardOrder.builder()
                .id(id1)
                .agent(Employee.builder().id(agentId1).build())
                .creditLimit(creditLimit1)
                .client(client1)
                .build());
        originalList.add(CardOrder.builder()
                .id(id2)
                .agent(Employee.builder().id(agentId2).build())
                .creditLimit(creditLimit2)
                .client(client2)
                .build());

        var expectedListDTO = new ArrayList<CardOrderDTO>();
        expectedListDTO.add(CardOrderDTO.builder()
                .id(id1)
                .agent(EmployeeDTOId.builder().id(agentId1).build())
                .creditLimit(creditLimit1)
                .client(client1)
                .build());
        expectedListDTO.add(CardOrderDTO.builder()
                .id(id2)
                .agent(EmployeeDTOId.builder().id(agentId2).build())
                .creditLimit(creditLimit2)
                .client(client2)
                .build());

        var fromDate = LocalDateTime.of(2000,3,4, 0,0,0);
        var toDate = LocalDateTime.of(2000,3,5, 23,59,59);

        given(cardOrderService.findByCreatedOnBetweenOrderByCreatedOn(fromDate, toDate))
                .willReturn(originalList);

        // when
        var resultActions = mockMvc.perform(
                get("/card-order/find-by-created-on")
                        .param("fromDate", fromDate.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("toDate", toDate.format(DateTimeFormatter.ISO_DATE_TIME)));

        // then
        resultActions.andExpect(status().isOk());

        var result = resultActions.andReturn();

        List<CardOrderDTO> actualListDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<CardOrderDTO>>() {});

        assertThat(actualListDTO.size()).isEqualTo(expectedListDTO.size());
        assertThat(actualListDTO).isEqualTo(expectedListDTO);
    }

    @SneakyThrows
    @Test
    public void givenPeriod_whenGetTopAgents_thenReturnListOfMap(){
        // given
        var fromDate = LocalDateTime.of(2000,3,4, 0,0,0);
        var toDate = LocalDateTime.of(2000,3,5, 23,59,59);

        var expectedList = new ArrayList<TopAgentsByOrdersDTO>();
        expectedList.add(TopAgentsByOrdersDTO.builder()
                .ordersCount(BigInteger.valueOf(5))
                .creditLimitSum(BigDecimal.valueOf(30000*100, 2))
                .agentId(2)
                .firstName("Firstname1")
                .lastName("Lastname1").build());

        given(cardOrderService.topAgentsByOrdersCount(fromDate, toDate))
                .willReturn(expectedList);
        // when
        var resultActions = mockMvc.perform(
                get("/card-order/top-agents-by-orders-count")
                    .param("fromDate", fromDate.format(DateTimeFormatter.ISO_DATE_TIME))
                    .param("toDate", toDate.format(DateTimeFormatter.ISO_DATE_TIME)));

        // then
        resultActions.andExpect(status().isOk());

        var result = resultActions.andReturn();

        var actualList = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                    new TypeReference<List<TopAgentsByOrdersDTO>>() {});

        assertThat(actualList.size()).isEqualTo(expectedList.size());
        assertThat(actualList).isEqualTo(expectedList);
    }

    @SneakyThrows
    @Test
    public void givenPeriod_whenGetMonthlyTotals_thenReturnListOfMap(){
        // given
        var fromDate = LocalDateTime.of(2000,3,4, 0,0,0);
        var toDate = LocalDateTime.of(2000,3,5, 23,59,59);
        var currency = "USD";

        var expectedList = new ArrayList<MonthlyTotalsDTO>();
        expectedList.add(MonthlyTotalsDTO.builder()
                .monthBegin(LocalDateTime.of(2000, 3, 4, 0, 0,0))
                .ordersCount(BigInteger.valueOf(30000))
                .creditLimit(BigDecimal.valueOf(11000*100,2)).build());

        given(cardOrderService.monthlyTotals(fromDate, toDate, currency))
                .willReturn(expectedList);
        // when
        var resultActions = mockMvc.perform(
                get("/card-order/monthly-totals")
                    .param("fromDate", fromDate.format(DateTimeFormatter.ISO_DATE_TIME))
                    .param("toDate", toDate.format(DateTimeFormatter.ISO_DATE_TIME))
                    .param("currency", "USD"));

        // then
        resultActions.andExpect(status().isOk());

        var result = resultActions.andReturn();

        List<MonthlyTotalsDTO> actualList = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                    new TypeReference<List<MonthlyTotalsDTO>>() {});

        assertThat(actualList.size()).isEqualTo(expectedList.size());
        assertThat(actualList).isEqualTo(expectedList);
    }

    private CardOrderDTO cardOrderDTO(){
        return CardOrderDTO.builder()
                .agent(EmployeeDTOId.builder().id(2).build())
                .creditLimit(new BigDecimal(100))
                .client("Client1")
                .build();
    }

    private CardOrderDTO incorrectCardOrderDTO(){
        return CardOrderDTO.builder()
                .agent(null) // должен не не null
                .creditLimit(new BigDecimal(0)) // должен быть >= 1
                .client("C") // слишком короткое
                .build();
    }


}
