package com.mariuszgajewski.rewards.adapters.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mariuszgajewski.rewards.domain.exception.ApiErrorResponse;
import com.mariuszgajewski.rewards.domain.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations =
        { "classpath:application-test.properties"})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RewardsControllerIT {

    private static final String USER_ID_1 = "USER1";
    private static final String USER_ID_2 = "USER2";
    private static final String TRANSACTION_CURRENCY = "USD";
    private static final double TRANSACTION_AMOUNT_1 = 120;
    private static final double TRANSACTION_AMOUNT_2 = 140;
    private static final ZonedDateTime TRANSACTION_DATE = ZonedDateTime.parse("2023-04-03T12:05:45.231Z");
    private final ObjectMapper OBJECT_MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    @Autowired
    private MockMvc mockRewardsController;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    private List<UserTransaction> prepareUserTransactions() {
        List<UserTransaction> userTransactions = new ArrayList<>();

        UserTransaction userTransaction = UserTransaction.builder()
                .transactionDate(TRANSACTION_DATE)
                .amount(TRANSACTION_AMOUNT_1)
                .currency(TRANSACTION_CURRENCY)
                .build();
        userTransactions.add(userTransaction);
        userTransaction = UserTransaction.builder()
                .transactionDate(TRANSACTION_DATE.minusMonths(3))
                .amount(TRANSACTION_AMOUNT_1)
                .currency(TRANSACTION_CURRENCY)
                .build();
        userTransactions.add(userTransaction);
        return userTransactions;
    }
    @Test
    @Order(1)
    void createTransactionsTest() throws Throwable {
        //given
        CreateTransactionsRequest request = new CreateTransactionsRequest();
        request.setUserId(USER_ID_1);
        request.setTransactions(prepareUserTransactions());

        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                .post("/rewards-service/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        TransactionResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getUserId()).isEqualTo(USER_ID_1);
        assertThat(response.getTransactions()).hasSize(2);
        assertThat(response.getTransactions().get(0).getTransactionDate()).isEqualTo(TRANSACTION_DATE);
        assertThat(response.getTransactions().get(0).getCurrency()).isEqualTo(TRANSACTION_CURRENCY);
        assertThat(response.getTransactions().get(0).getAmount()).isEqualTo(TRANSACTION_AMOUNT_1);
        assertThat(response.getTransactions().get(0).getTransactionId()).isEqualTo(1);
        assertThat(response.getTransactions().get(1).getTransactionDate()).isEqualTo(TRANSACTION_DATE.minusMonths(3));
        assertThat(response.getTransactions().get(1).getCurrency()).isEqualTo(TRANSACTION_CURRENCY);
        assertThat(response.getTransactions().get(1).getAmount()).isEqualTo(TRANSACTION_AMOUNT_1);
        assertThat(response.getTransactions().get(1).getTransactionId()).isEqualTo(2);
    }

    @Test
    @Order(2)
    void updateTransactionTest() throws Throwable {
        //given
        UserTransaction userTransaction = UserTransaction.builder()
                .transactionDate(TRANSACTION_DATE.plusDays(1))
                .amount(TRANSACTION_AMOUNT_2)
                .currency(TRANSACTION_CURRENCY)
                .build();
        UpdateTransactionRequest request = new UpdateTransactionRequest();
        request.setUserId(USER_ID_1);
        request.setTransaction(userTransaction);

        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .patch("/rewards-service/transaction/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        TransactionResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getUserId()).isEqualTo(USER_ID_1);
        assertThat(response.getTransactions()).hasSize(1);
        assertThat(response.getTransactions().get(0).getTransactionDate()).isEqualTo(TRANSACTION_DATE.plusDays(1));
        assertThat(response.getTransactions().get(0).getCurrency()).isEqualTo(TRANSACTION_CURRENCY);
        assertThat(response.getTransactions().get(0).getAmount()).isEqualTo(TRANSACTION_AMOUNT_2);
        assertThat(response.getTransactions().get(0).getTransactionId()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void getTransactionTest() throws Throwable {
        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .get("/rewards-service/transaction/1"))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        TransactionResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getUserId()).isEqualTo(USER_ID_1);
        assertThat(response.getTransactions()).hasSize(1);
        assertThat(response.getTransactions().get(0).getTransactionDate()).isEqualTo(TRANSACTION_DATE.plusDays(1));
        assertThat(response.getTransactions().get(0).getCurrency()).isEqualTo(TRANSACTION_CURRENCY);
        assertThat(response.getTransactions().get(0).getAmount()).isEqualTo(TRANSACTION_AMOUNT_2);
        assertThat(response.getTransactions().get(0).getTransactionId()).isEqualTo(1);
    }

    @Test
    @Order(4)
    void calculateRewardPointsTest() throws Throwable {
        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .get("/rewards-service/calculate/" + USER_ID_1))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        CalculationResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), CalculationResponse.class);
        assertThat(response.getCustomerId()).isEqualTo(USER_ID_1);
        assertThat(response.getTotalValue()).isEqualTo(130);
        assertThat(response.getMonthlyRewards()).hasSize(1);
        assertThat(response.getMonthlyRewards()).containsEntry("April", 130L);
    }

    @Test
    @Order(5)
    void removeTransactionTest() throws Throwable {
        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                .delete("/rewards-service/transaction/1"))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        TransactionResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getUserId()).isEqualTo(USER_ID_1);
        assertThat(response.getTransactions()).hasSize(1);
        assertThat(response.getTransactions().get(0).getTransactionDate()).isEqualTo(TRANSACTION_DATE.plusDays(1));
        assertThat(response.getTransactions().get(0).getCurrency()).isEqualTo(TRANSACTION_CURRENCY);
        assertThat(response.getTransactions().get(0).getAmount()).isEqualTo(TRANSACTION_AMOUNT_2);
        assertThat(response.getTransactions().get(0).getTransactionId()).isEqualTo(1);
    }

    @Test
    void updateNonExistingTransaction() throws Throwable {
        //given
        UserTransaction userTransaction = UserTransaction.builder()
                .transactionDate(TRANSACTION_DATE)
                .amount(TRANSACTION_AMOUNT_2)
                .currency(TRANSACTION_CURRENCY)
                .build();
        UpdateTransactionRequest request = new UpdateTransactionRequest();
        request.setUserId(USER_ID_1);
        request.setTransaction(userTransaction);

        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .patch("/rewards-service/transaction/1105")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(404);
    }

    @Test
    void getNonExistingTransactionTest() throws Throwable {
        //when //then
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .get("/rewards-service/transaction/1234"))
                .andExpect(status().isNotFound())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(404);
    }

    @Test
    void calculateRewardPointsMissingTransactionsTest() throws Throwable {
        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .get("/rewards-service/calculate/" + USER_ID_2))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        CalculationResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), CalculationResponse.class);
        assertThat(response.getCustomerId()).isEqualTo(USER_ID_2);
        assertThat(response.getTotalValue()).isZero();
        assertThat(response.getMonthlyRewards()).isEmpty();
    }

    @Test
    void createTransactionWOBodyTest() throws Throwable {
        //when //then
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .post("/rewards-service/transaction"))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
    }

    @Test
    void createTransactionMissingAmountTest() throws Throwable {
        //given
        UserTransaction userTransaction = UserTransaction.builder()
                .transactionDate(TRANSACTION_DATE)
                .currency(TRANSACTION_CURRENCY)
                .build();
        CreateTransactionsRequest request = new CreateTransactionsRequest();
        request.setUserId(USER_ID_1);
        request.setTransactions(List.of(userTransaction));

        //when
        MvcResult mcvResult = mockRewardsController.perform(MockMvcRequestBuilders
                        .post("/rewards-service/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");

        //then
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo("Transaction amount is missing\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(400);
    }
}