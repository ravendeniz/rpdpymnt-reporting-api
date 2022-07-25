package com.rpdpymnt.reporting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rpdpymnt.reporting.dto.ErrorDetails;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@UtilityClass
@Slf4j
public class ResponseUtil {

    public static void sendError(int status, Exception ex, PrintWriter out) {
        try {
            ErrorDetails errorDetails = new ErrorDetails(status, ex.getMessage());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(errorDetails);
            out.print(json);
            out.flush();
        } catch (JsonProcessingException jpe) {
            log.error("Error sending error response for authentication", jpe);
        }
    }
}
