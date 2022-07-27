package com.rpdpymnt.reporting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpdpymnt.reporting.dto.ClientResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import com.rpdpymnt.reporting.repository.UserProfileRepository;
import com.rpdpymnt.reporting.service.ClientService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerUnitTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ClientService clientService;

    @MockBean
    UserProfileRepository userProfileRepository;

    @MockBean
    RestTemplateBuilder restTemplateBuilder;

    @MockBean
    RestTemplate restTemplate;

    private ClientResponse clientResponse;

    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/test/resources/ClientResponse.json");
        clientResponse = objectMapper.readValue(file, ClientResponse.class);
    }

    @Test
    void ReportRequestRequestWhenTransactionsReportThenResultOk() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionId("1-1444392550-1");

        Mockito.when(clientService.getClientInfo(transactionRequest)).thenReturn(clientResponse);
        String url = "/api/client";

        MvcResult mvcResult = mockMvc.perform(post(url).content(objectMapper.writeValueAsString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }
}
