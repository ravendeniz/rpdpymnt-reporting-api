package com.rpdpymnt.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionListDataResponse {

    private FxResponse fx;
    private CustomerInfoResponse customerInfo;
    private MerchantResponse merchant;
    private IpnResponse ipn;
    private MerchantTransactionsResponse transaction;
    private AcquirerResponse acquirer;
    private boolean refundable;
}
