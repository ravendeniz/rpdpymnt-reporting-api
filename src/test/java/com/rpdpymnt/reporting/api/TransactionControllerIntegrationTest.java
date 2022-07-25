package com.rpdpymnt.reporting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpdpymnt.reporting.dto.ReportRequest;
import com.rpdpymnt.reporting.service.TransactionService;
import com.rpdpymnt.reporting.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    void transactionList() throws Exception {
        ReportRequest reportRequest = ReportRequest.builder()
                .fromDate(DateUtil.convertStringToLocalDate("2015-01-01"))
                .toDate(DateUtil.convertStringToLocalDate("2015-01-01"))
                .acquirer(1)
                .merchant(1)
                .build();
        String json = mapper.writeValueAsString(reportRequest);
        mockMvc.perform(post("transactions/report").content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
