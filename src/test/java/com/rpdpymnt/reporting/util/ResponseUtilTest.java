package com.rpdpymnt.reporting.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResponseUtilTest {

    @Test
    public void givenExceptionWhenSendErrorThenVerifyJsonProcessingException() {
        // Arrange
        Exception exc = mock(Exception.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        given(exc.getMessage()).willAnswer(invocation -> {
            throw new JsonGenerationException(null, null, null);
        });
        // Act
        ResponseUtil.sendError(1, exc, printWriter);
        // Assert
        verify(printWriter, times(0)).print(anyString());
    }
}
