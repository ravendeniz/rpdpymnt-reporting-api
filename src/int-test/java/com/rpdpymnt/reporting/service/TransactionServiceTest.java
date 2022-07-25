package com.rpdpymnt.reporting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @MockBean
    ExternalTokenService externalTokenService;
    RestTemplate restTemplate;

    TransactionService transactionService;

    @BeforeEach
    void initUseCase() {
        transactionService = new DefaultTransactionService(externalTokenService, restTemplate);
    }
}
