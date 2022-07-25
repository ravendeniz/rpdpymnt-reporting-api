package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.ExternalTokenRequest;
import com.rpdpymnt.reporting.dto.ExternalTokenResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DefaultExternalTokenService extends BaseService implements ExternalTokenService {

    protected final RestTemplate restTemplate;

    private static String approved = "APPROVED";
    private static String tokenUrl = "https://sandbox-reporting.rpdpymnt.com/api/v3/merchant/user/login";

    @Override
    public String getExternalToken() {
        ExternalTokenResponse response = null;
        try {
            ExternalTokenRequest body = ExternalTokenRequest.builder().email("demo@financialhouse.io")
                    .password("cjaiU8CV").build();
            response = restTemplate.postForEntity(tokenUrl, asHttpEntity(body), ExternalTokenResponse.class).getBody();
            final String status = response.getStatus();
            if(!StringUtils.equalsIgnoreCase(status, approved)) {
                throw new SecurityException("Token problem");
            }
            return response.getToken();
        } catch (Exception e) {
            throw new SecurityException("Token problem");
        }
    }
}
