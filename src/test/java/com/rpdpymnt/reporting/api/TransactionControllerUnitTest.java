package com.rpdpymnt.reporting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import com.rpdpymnt.reporting.dto.TransactionResponse;
import com.rpdpymnt.reporting.service.TransactionService;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.when;

public class TransactionControllerUnitTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Mock
    TransactionService transactionService;

    @InjectMocks
    TransactionController controller;

    private TransactionResponse transactionResponse;

    @Before
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        transactionResponse = objectMapper.readValue(new File("TransactionResponse.json"),
                TransactionResponse.class);
    }

    @Test
    void givenTransactionRequestWhenTransactionThenRerultTransactionResponse() {
        final TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionId("1-1444392550-1");

        when(transactionService.getTransaction(transactionRequest)).thenReturn(transactionResponse);

        ResponseEntity<TransactionResponse> result = controller.transaction(transactionRequest);

        softly.assertThat(result.getBody().getMerchant().getName()).isEqualTo("Dev-Merchant");
    }
}
