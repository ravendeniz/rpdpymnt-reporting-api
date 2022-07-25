package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.ReportRequest;
import com.rpdpymnt.reporting.dto.ReportResponse;
import com.rpdpymnt.reporting.dto.TransactionListRequest;
import com.rpdpymnt.reporting.dto.TransactionListResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;
import com.rpdpymnt.reporting.dto.TransactionResponse;

public interface TransactionService {

    ReportResponse getReport(ReportRequest reportRequest);
    TransactionListResponse getTransactionList(TransactionListRequest transactionListRequest);
    TransactionResponse getTransaction(TransactionRequest transactionRequest);
}
