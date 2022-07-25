package com.rpdpymnt.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionListRequest {

    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
    private String operation;
    private int merchantId;
    private int acquirerId;
    private String paymentMethod;
    private String errorCode;
    private String filterField;
    private String filterValue;
    private String page;
}
