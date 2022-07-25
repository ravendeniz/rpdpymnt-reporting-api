package com.rpdpymnt.reporting.security;

import com.rpdpymnt.reporting.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SecurityFilterTest {

    private static final String AUTHUSER_ID_TOKEN = "authorization";
    private static final String REQUEST_URL = "http://localhost/request/1";
    private static final String TOKEN = "token1";
    private static final String EXCLUDE_URL1 = "/excludedUrl";

    private final TokenService tokenService = mock(TokenService.class);
    private final HttpServletRequest mockReq = mock(HttpServletRequest.class);
    private final HttpServletResponse mockResp = mock(MockHttpServletResponse.class);
    private final FilterChain mockFilterChain = mock(FilterChain.class);
    private SecurityFilter securityFilter;

    @Before
    public void setUp() {
        securityFilter = new SecurityFilter(tokenService, new String[]{EXCLUDE_URL1});
    }

    @Test
    public void givenInvalidTokenWhenDoFilterInternalThenResponseSecurityException() throws Exception {
        // Arrange
        when(mockReq.getRequestURL()).thenReturn(new StringBuffer(REQUEST_URL));
        when(mockReq.getHeader(AUTHUSER_ID_TOKEN)).thenReturn(TOKEN);
        PrintWriter writer = mock(PrintWriter.class);
        when(mockResp.getWriter()).thenReturn(writer);
        when(tokenService.validate(anyString())).thenThrow(new SecurityException("failed"));
        // Act
        securityFilter.doFilterInternal(mockReq, mockResp, mockFilterChain);
        // Assert
        verify(mockResp).getWriter();
        final String separator = System.getProperty("line.separator");
        verify(writer).print(
                "{" + separator + "  \"code\" : 403," + separator + "  \"message\" : \"failed\""
                        + separator + "}");
    }

    @Test
    public void givenEmptyTokenWhenDoFilterInternalThenResponse401SecurityExceptionWhenTokenEmpty() throws Exception {
        // Arrange
        when(mockReq.getRequestURL()).thenReturn(new StringBuffer(REQUEST_URL));
        when(mockReq.getHeader(AUTHUSER_ID_TOKEN)).thenReturn(null);
        PrintWriter writer = mock(PrintWriter.class);
        when(mockResp.getWriter()).thenReturn(writer);
        when(tokenService.validate(anyString())).thenThrow(new SecurityException("Access token is invalid"));
        // Act
        securityFilter.doFilterInternal(mockReq, mockResp, mockFilterChain);
        // Assert
        verify(mockResp).getWriter();
        final String separator = System.getProperty("line.separator");
        verify(writer).print(
                "{" + separator + "  \"code\" : 401," + separator + "  \"message\" : \"No authentication token found\""
                        + separator + "}");
    }

}
