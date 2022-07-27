package com.rpdpymnt.reporting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpdpymnt.reporting.dto.ReportRequest;
import com.rpdpymnt.reporting.dto.ReportResponse;
import com.rpdpymnt.reporting.dto.TransactionListRequest;
import com.rpdpymnt.reporting.dto.TransactionListResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import com.rpdpymnt.reporting.dto.TransactionResponse;
import com.rpdpymnt.reporting.repository.UserProfileRepository;
import com.rpdpymnt.reporting.service.TransactionService;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerUnitTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TransactionService transactionService;

    @MockBean
    UserProfileRepository userProfileRepository;

    @MockBean
    RestTemplateBuilder restTemplateBuilder;

    @MockBean
    RestTemplate restTemplate;

    private TransactionResponse transactionResponse;
    private TransactionListResponse transactionListResponse;
    private ReportResponse reportResponse;

    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/test/resources/TransactionResponse.json");
        transactionResponse = objectMapper.readValue(file, TransactionResponse.class);
        file = new File("src/test/resources/TransactionListResponse.json");
        transactionListResponse = objectMapper.readValue(file, TransactionListResponse.class);
        file = new File("src/test/resources/ReportResponse.json");
        reportResponse = objectMapper.readValue(file, ReportResponse.class);
    }

    @Test
    void ReportRequestRequestWhenTransactionsReportThenResultOk() throws Exception {
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setFromDate(LocalDate.parse("2015-07-01"));
        reportRequest.setToDate(LocalDate.parse("2015-10-01"));
        reportRequest.setMerchant(1);
        reportRequest.setAcquirer(1);

        Mockito.when(transactionService.getReport(reportRequest)).thenReturn(reportResponse);
        String url = "/api/transactions/report";

        MvcResult mvcResult = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(reportRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    @Test
    void givenTransactionListRequestWhenTransactionListThenResultOk() throws Exception {
        TransactionListRequest transactionListRequest = new TransactionListRequest();
        transactionListRequest.setStatus("DECLINED");
        transactionListRequest.setOperation("3D");
        transactionListRequest.setErrorCode("Invalid Transaction");

        Mockito.when(transactionService.getTransactionList(transactionListRequest)).thenReturn(transactionListResponse);
        String url = "/api/transaction/list";

        MvcResult mvcResult = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(transactionListRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    @Test
    void givenTransactionRequestWhenTransactionThenResultOk() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionId("1-1444392550-1");

        Mockito.when(transactionService.getTransaction(transactionRequest)).thenReturn(transactionResponse);
        String url = "/api/transaction";

        MvcResult mvcResult = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }
}
