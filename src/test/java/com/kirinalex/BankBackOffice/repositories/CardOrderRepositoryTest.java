package com.kirinalex.BankBackOffice.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.models.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Тестирум на постгре а не на H2, т.к. при  H2 возникают ошибки создания таблицы Contacts,
// а также при выполнении monthlyTotals возникает ошибка Value too long for column "NUMERIC"
class CardOrderRepositoryTest {

    @Autowired
    private CardOrderRepository cardOrderRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @MockBean
    private ObjectMapper objectMapper;

    @Test
    void givenCardOrders_whenGetMonthlyTotals_thenReturnListOfMap() {
        // given
        var fromDate = LocalDateTime.of(2000,2, 1, 0, 0, 0);
        var toDate = LocalDateTime.of(2000, 5, 31,23,59,59, 999999*1000);
        var agent = createEmployee();
        var client = "Client1";

        createCardOrder(fromDate, 2000, agent, client);                // в периоде
        createCardOrder(fromDate.plusDays(1), 20000, agent, client);   // в периоде
        createCardOrder(fromDate.plusMonths(1), 500, agent, client);   // в периоде
        createCardOrder(fromDate.minusNanos(1000), 10, agent, client); // до периода
        createCardOrder(toDate.plusNanos(1000), 20, agent, client);    // после периода

        var expectedList = new ArrayList<Map<String, Object>>();
        expectedList.add(Map.of(
                "month_begin", Timestamp.valueOf(LocalDateTime.of(2000, 2, 1, 0, 0,0)),
                "orders_count", BigInteger.valueOf(2),
                "credit_limit_sum", BigDecimal.valueOf(11000*100,2)));

        expectedList.add(Map.of(
                "month_begin", Timestamp.valueOf(LocalDateTime.of(2000, 3, 1, 0, 0,0)),
                "orders_count", BigInteger.valueOf(1),
                "credit_limit_sum", BigDecimal.valueOf(250*100, 2)));

        // when
        var actualRawList = cardOrderRepository.monthlyTotals(fromDate, toDate, 2);

        // then
        assertThat(actualRawList.size()).isEqualTo(expectedList.size());

        var actualList = convertToMapList(actualRawList);
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void givenCardOrders_whenGetTopAgents_thenReturnListOfMap() {

        // given
        var fromDate = LocalDateTime.of(2000,2, 1, 0, 0, 0);
        var toDate = LocalDateTime.of(2000, 5, 31,23,59,59, 999999999);
        var client = "Client1";
        var agent1 = createEmployee();
        var agent2 = createEmployee();
        var agent3 = createEmployee();
        var agent4 = createEmployee();

        // 1
        createCardOrder(fromDate, 100, agent1, client);
        createCardOrder(fromDate, 100, agent1, client);
        // 2
        createCardOrder(fromDate, 100, agent2, client);
        createCardOrder(fromDate, 100, agent2, client);
        createCardOrder(fromDate, 100, agent2, client);
        createCardOrder(fromDate, 100, agent2, client);
        // 3
        createCardOrder(fromDate, 100, agent3, client);
        // 4
        createCardOrder(fromDate, 100, agent4, client);
        createCardOrder(fromDate, 100, agent4, client);
        createCardOrder(fromDate, 100, agent4, client);

        var expectedList = new ArrayList<Map<String, Object>>();
        expectedList.add(Map.of(
                "orders_count", BigInteger.valueOf(4),
                "credit_limit_sum", BigDecimal.valueOf(400*100, 2),
                "agent_id", agent2.getId(),
                "first_name", "firstName",
                "last_name", "lastName"));

        expectedList.add(Map.of(
                "orders_count", BigInteger.valueOf(3),
                "credit_limit_sum", BigDecimal.valueOf(300*100, 2),
                "agent_id", agent4.getId(),
                "first_name", "firstName",
                "last_name", "lastName"));

        expectedList.add(Map.of(
                "orders_count", BigInteger.valueOf(2),
                "credit_limit_sum", BigDecimal.valueOf(200*100, 2),
                "agent_id", agent1.getId(),
                "first_name", "firstName",
                "last_name", "lastName"));

        // when
        var actualRawList = cardOrderRepository.topAgentsByOrdersCount(fromDate, toDate);

        // then
        assertThat(actualRawList.size()).isEqualTo(expectedList.size());

        var actualList = convertToMapList(actualRawList);
        assertThat(actualList).isEqualTo(expectedList);
    }

    private void createCardOrder(LocalDateTime createdOn, int creditLimit, Employee agent, String client){
        var cardOrder = CardOrder.builder()
                .createdOn(createdOn)
                .creditLimit(new BigDecimal(creditLimit))
                .agent(agent)
                .client(client)
                .build();

        cardOrderRepository.save(cardOrder);
    }

    private Employee createEmployee() {
        var employee = Employee.builder()
                .firstName("firstName")
                .lastName("lastName")
                .birthday(LocalDate.of(2000,1,1))
                .build();

        employeeRepository.save(employee);

        return employee;
    }

    // Конвертируем в List<Map>, для возможности сравнения в assertThat с List<Map>
    // т.к. результат выборки из репозитория имеет тип List<AbstractJpaQuery$TupleConverter$TupleBackedMap>
    // и assertThat выдает что списки не равны
    private List<Map<String, Object>> convertToMapList(List<Map<String, Object>> list) {
        return list.stream().map(record -> record.entrySet().stream().collect(
                        Collectors.toMap(e -> e.getKey(), e -> e.getValue())))
                .collect(Collectors.toList());

    }
}