package com.rpdpymnt.reporting.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void transactionList() throws Exception {
        mockMvc.perform(post("transactions/report"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
