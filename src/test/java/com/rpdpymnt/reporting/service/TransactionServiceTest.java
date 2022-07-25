package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.ReportRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    ExternalTokenService externalTokenService;

    @InjectMocks
    DefaultTransactionService transactionService;

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        transactionService = new DefaultTransactionService(externalTokenService, restTemplate);
    }

    @Test
    public void getReport() {
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setFromDate(LocalDate.parse("2015-07-01"));
        reportRequest.setToDate(LocalDate.parse("2015-07-01"));
        reportRequest.setAcquirer(1);
        reportRequest.setMerchant(1);
        transactionService.getReport(reportRequest);
    }
}
