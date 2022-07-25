package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.ClientResponse;
import com.rpdpymnt.reporting.dto.TransactionRequest;

public interface ClientService {

    ClientResponse getClientInfo(TransactionRequest transactionRequest);
}
