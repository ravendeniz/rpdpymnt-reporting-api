package com.rpdpymnt.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantTransactionResponse {

    private String referenceNo;
    private int merchantId;
    private String status;
    private String channel;
    private Object customData;
    private String chainId;
    private int agentInfoId;
    private String operation;
    private int fxTransactionId;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private int id;
    private int acquirerTransactionId;
    private String code;
    private String message;
    private String transactionId;
}
