package com.rpdpymnt.reporting.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class BaseService {

    public <T> HttpEntity<T> asHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(body, headers);
    }

    public <T> HttpEntity<T> asHttpEntityWithHeader(T body, HttpHeaders headers) {
        return new HttpEntity<>(body, headers);
    }
}
