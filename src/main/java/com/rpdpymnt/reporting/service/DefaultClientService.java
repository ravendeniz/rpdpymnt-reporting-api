package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.ClientResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DefaultClientService extends BaseService implements ClientService {

    private final ExternalTokenService externalTokenService;
    protected final RestTemplate restTemplate;

    private static String clientUrl = "https://sandbox-reporting.rpdpymnt.com/api/v3/client";

    @Override
    public ClientResponse getClientInfo(TransactionRequest transactionRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getExternalToken());

            ClientResponse response = restTemplate
                    .postForEntity(clientUrl, asHttpEntityWithHeader(transactionRequest, headers), ClientResponse.class)
                    .getBody();
            return response;
        } catch (Exception e) {
            return null;
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
