package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.ReportRequest;
import com.rpdpymnt.reporting.dto.ReportResponse;
import com.rpdpymnt.reporting.dto.TransactionListRequest;
import com.rpdpymnt.reporting.dto.TransactionListResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import com.rpdpymnt.reporting.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DefaultTransactionService extends BaseService implements TransactionService {

    private final ExternalTokenService externalTokenService;
    protected final RestTemplate restTemplate;

    private static String reportUrl = "https://sandbox-reporting.rpdpymnt.com/api/v3/transactions/report";
    private static String transactionListUrl = "https://sandbox-reporting.rpdpymnt.com/api/v3/transaction/list";
    private static String transactionUrl = "https://sandbox-reporting.rpdpymnt.com/api/v3/transaction";


    @Override
    public ReportResponse getReport(ReportRequest reportRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getExternalToken());
            ReportResponse response = restTemplate
                    .postForEntity(reportUrl, asHttpEntityWithHeader(reportRequest, headers), ReportResponse.class)
                    .getBody();
            return response;
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public TransactionListResponse getTransactionList(TransactionListRequest transactionListRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getExternalToken());
            TransactionListResponse response = restTemplate
                    .postForEntity(transactionListUrl, asHttpEntityWithHeader(transactionListRequest, headers), TransactionListResponse.class)
                    .getBody();
            return response;
        } catch (Exception e) {
            throw new SecurityException("Service Problem");
        }
    }

    @Override
    public TransactionResponse getTransaction(TransactionRequest transactionRequest) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getExternalToken());
            TransactionResponse response = restTemplate.postForEntity(transactionUrl, asHttpEntityWithHeader(transactionRequest, headers), TransactionResponse.class)
                    .getBody();
            return response;
        } catch (Exception e) {
            throw new SecurityException("Service Problem");
        }
    }

    private String getExternalToken() {
        final String token = externalTokenService.getExternalToken();
        if(StringUtils.isEmpty(token)) {
            throw new SecurityException("Token problem");
        }
        return token;
    }
}
