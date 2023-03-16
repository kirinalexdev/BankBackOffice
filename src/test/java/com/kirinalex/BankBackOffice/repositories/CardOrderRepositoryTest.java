package com.kirinalex.BankBackOffice.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.dao.CardOrderDAO;
import com.kirinalex.BankBackOffice.dto.MonthlyTotalsDTO;
import com.kirinalex.BankBackOffice.dto.TopAgentsByOrdersDTO;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.models.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CardOrderDAO.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Тестируeм на постгре а не на H2, т.к. при H2 возникают ошибки создания таблицы Contacts,
// а также при выполнении monthlyTotals возникает ошибка Value too long for column "NUMERIC"
class CardOrderRepositoryTest {

    @Autowired
    private CardOrderRepository cardOrderRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CardOrderDAO cardOrderDAO;

    @MockBean
    private ObjectMapper objectMapper;

    @Test
    void givenCardOrders_whenGetMonthlyTotals_thenReturnListOfDTO() {
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

        var expectedList = new ArrayList<MonthlyTotalsDTO>();
        expectedList.add(MonthlyTotalsDTO.builder()
                        .monthBegin(LocalDateTime.of(2000, 2, 1, 0, 0,0))
                        .ordersCount(BigInteger.valueOf(2))
                        .creditLimit(BigDecimal.valueOf(11000*100,2)).build());

        expectedList.add(MonthlyTotalsDTO.builder()
                        .monthBegin(LocalDateTime.of(2000, 3, 1, 0, 0,0))
                        .ordersCount(BigInteger.valueOf(1))
                        .creditLimit(BigDecimal.valueOf(250*100,2)).build());

        // when
        var actualList = cardOrderDAO.monthlyTotals(fromDate, toDate, 2);

        // then
       assertThat(actualList.size()).isEqualTo(expectedList.size());
       assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void givenCardOrders_whenGetTopAgents_thenReturnListOfDTO() {

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

        var expectedList = new ArrayList<TopAgentsByOrdersDTO>();
        expectedList.add(TopAgentsByOrdersDTO.builder()
                        .ordersCount(BigInteger.valueOf(4))
                        .creditLimitSum(BigDecimal.valueOf(400*100, 2))
                        .agentId(agent2.getId())
                        .firstName("firstName")
                        .lastName("lastName").build());

        expectedList.add(TopAgentsByOrdersDTO.builder()
                        .ordersCount(BigInteger.valueOf(3))
                        .creditLimitSum(BigDecimal.valueOf(300*100, 2))
                        .agentId(agent4.getId())
                        .firstName("firstName")
                        .lastName("lastName").build());

        expectedList.add(TopAgentsByOrdersDTO.builder()
                        .ordersCount(BigInteger.valueOf(2))
                        .creditLimitSum(BigDecimal.valueOf(200*100, 2))
                        .agentId(agent1.getId())
                        .firstName("firstName")
                        .lastName("lastName").build());

        // when
        var actualList = cardOrderDAO.topAgentsByOrdersCount(fromDate, toDate);

        // then
        assertThat(actualList.size()).isEqualTo(expectedList.size());

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
}
