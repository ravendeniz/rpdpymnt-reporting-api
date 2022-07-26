package com.rpdpymnt.reporting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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
    @JsonProperty("updated_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;
    @JsonProperty("created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
    private int id;
    private int acquirerTransactionId;
    private String code;
    private String message;
    private String transactionId;
}
