package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.Application;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

import static com.rpdpymnt.reporting.api.IntegrationTestConfig.AUTH_TOKEN_HEADER;
import static com.rpdpymnt.reporting.api.IntegrationTestConfig.VALID_TOKEN;
import static com.rpdpymnt.reporting.api.IntegrationTestConfig.VALID_TOKEN_WITH_ADMIN_ROLE;
import static com.rpdpymnt.reporting.api.IntegrationTestConfig.VALID_TOKEN_WITH_DELETABLE_USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=int-test", webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
        IntegrationTestConfig.class, Application.class})
@AutoConfigureJsonTesters
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
abstract class BaseIT {

    @Autowired
    TestRestTemplate restTemplate;

    <T> ResponseEntity<T> unsecurePostForEntity(String url, HttpEntity httpEntity, Class<T> responseType,
            Object... urlVars) {
        return restTemplate.postForEntity(url, httpEntity, responseType, urlVars);
    }

    <T> ResponseEntity<T> unsecureGetForEntity(String url, HttpEntity httpEntity, Class<T> responseType,
            Object... urlVars) {
        return restTemplate.exchange(url, GET, httpEntity, responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetForEntity(String url, Class<T> responseType, Object... urlVars) {
        return restTemplate.exchange(url, GET, asHttpEntity(null), responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetWithAdminForEntity(String url, Class<T> responseType, Object... urlVars) {
        return restTemplate.exchange(url, GET, asHttpEntityWithAdmin(null), responseType, urlVars);
    }

    <T> ResponseEntity<T> securePostForEntity(String url, Object model, Class<T> responseType, Object... urlVars) {
        return restTemplate.postForEntity(url, asHttpEntity(model), responseType, urlVars);
    }

    <T> ResponseEntity<T> securePostWithAdminForEntity(String url, Object model, Class<T> responseType,
            Object... urlVars) {
        return restTemplate.postForEntity(url, asHttpEntityWithAdmin(model), responseType, urlVars);
    }

    <T> ResponseEntity<T> securePutWithAdminForEntity(String url, Object model, Class<T> responseType,
            Object... urlVars) {
        return restTemplate.exchange(url, PUT, asHttpEntityWithAdmin(model), responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetWithAdminForEntityWithCustomHeaders(String url,
            Class<T> responseType, Map<String, String> customHeaders, Object... urlVars) {
        HttpHeaders headers = new HttpHeaders();
        customHeaders.forEach(headers::add);
        headers.add(AUTH_TOKEN_HEADER, VALID_TOKEN_WITH_ADMIN_ROLE);
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, GET, entity, responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetForEntityWithCustomHeaders(String url, Class<T> responseType,
            Map<String, String> customHeaders, Object... urlVars) {
        HttpHeaders headers = new HttpHeaders();
        customHeaders.forEach(headers::add);
        headers.add(AUTH_TOKEN_HEADER, VALID_TOKEN);
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, GET, entity, responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetForEntityWithCustomQueries(String url, Class<T> responseType,
            Map<String, String> customQueries, Object... urlVars) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(url);
        customQueries.forEach(builder::queryParam);
        String customUrl = builder.buildAndExpand(urlVars).encode().toUriString();
        return restTemplate.exchange(customUrl, GET, asHttpEntity(null), responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetForEntityWithCustomHeadersAndCustomQueries(String url, Class<T> responseType,
            Map<String, String> customHeaders, Map<String, String> customQueries, Object... urlVars) {
        HttpHeaders headers = new HttpHeaders();
        customHeaders.forEach(headers::add);
        headers.add(AUTH_TOKEN_HEADER, VALID_TOKEN);
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(url);
        customQueries.forEach(builder::queryParam);
        String customUrl = builder.buildAndExpand(urlVars).encode().toUriString();
        return restTemplate.exchange(customUrl, GET, entity, responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetForEntityWithAdminWithCustomHeadersAndCustomQueries(String url,
            Class<T> responseType, Map<String, String> customHeaders, Map<String, String> customQueries,
            Object... urlVars) {
        HttpHeaders headers = new HttpHeaders();
        customHeaders.forEach(headers::add);
        headers.add(AUTH_TOKEN_HEADER, VALID_TOKEN_WITH_ADMIN_ROLE);
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(url);
        customQueries.forEach(builder::queryParam);
        String customUrl = builder.buildAndExpand(urlVars).encode().toUriString();
        return restTemplate.exchange(customUrl, GET, entity, responseType, urlVars);
    }

    <T> ResponseEntity<T> secureGetForEntityWithAdminWithCustomQueries(String url, Class<T> responseType,
            Map<String, String> customQueries, Object... urlVars) {
        return secureGetForEntityWithAdminWithCustomHeadersAndCustomQueries(url, responseType, Collections.emptyMap(),
                customQueries, urlVars);
    }

    <T> ResponseEntity<T> secureDeleteForEntity(String url, Class<T> responseType, Object... urlVars) {
        return restTemplate.exchange(url, DELETE, asHttpEntity(null), responseType, urlVars);
    }

    <T> ResponseEntity<T> secureDeleteWithAdminTokenForEntity(String url, Class<T> responseType, Object... urlVars) {
        return restTemplate.exchange(url, DELETE, asHttpEntityWithAdmin(null), responseType, urlVars);
    }

    <T> ResponseEntity<T> secureDeleteWithDeletableUserTokenForEntity(String url, Class<T> responseType,
            Object... urlVars) {
        return restTemplate.exchange(url, DELETE, asHttpEntityWithDeletableUser(null), responseType, urlVars);
    }

    <T> ResponseEntity<T> securePatchWithAdminForEntity(String url, Object payload, Class<T> responseType,
            Object... urlVars) {
        return restTemplate.exchange(url, PATCH, asHttpEntityWithAdmin(payload), responseType, urlVars);
    }

    <T> ResponseEntity<T> securePatchForEntity(String url, Object payload, Class<T> responseType, Object... urlVars) {
        return restTemplate
                .exchange(url, PATCH, new HttpEntity<>(payload, getSecureJsonHeaders()), responseType, urlVars);
    }

    <T> ResponseEntity<T> securePutForEntity(String url, Object payload, Class<T> responseType,
            Map<String, String> customHeaders, Object... urlVars) {
        HttpHeaders headers = getSecureJsonHeaders();
        customHeaders.forEach(headers::add);
        return restTemplate
                .exchange(url, PUT, new HttpEntity<>(payload, headers), responseType, urlVars);
    }

    <T> ResponseEntity<T> securePutForEntity(String url, Object payload, Class<T> responseType, Object... urlVars) {
        return restTemplate
                .exchange(url, PUT, new HttpEntity<>(payload, getSecureJsonHeaders()), responseType, urlVars);
    }

    private <T> HttpEntity<T> asHttpEntityWithAdmin(T body) {
        return asHttpEntity(body, VALID_TOKEN_WITH_ADMIN_ROLE);
    }

    private <T> HttpEntity<T> asHttpEntityWithDeletableUser(T body) {
        return asHttpEntity(body, VALID_TOKEN_WITH_DELETABLE_USER);
    }

    private <T> HttpEntity<T> asHttpEntity(T body) {
        return asHttpEntity(body, VALID_TOKEN);
    }

    private <T> HttpEntity<T> asHttpEntity(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_TOKEN_HEADER, token);
        return new HttpEntity<>(body, headers);
    }

    private HttpHeaders getSecureJsonHeaders() {
        return getSecureJsonHeaders(VALID_TOKEN);
    }

    HttpHeaders getSecureJsonHeaders(String token) {
        HttpHeaders requestHeaders = getJsonHeaders();
        requestHeaders.add(AUTH_TOKEN_HEADER, token);
        return requestHeaders;
    }

    HttpHeaders getJsonHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

}
