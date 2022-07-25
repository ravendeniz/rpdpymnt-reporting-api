package com.rpdpymnt.reporting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionListResponse {

    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("current_page")
    private int currentPage;
    @JsonProperty("next_page_url")
    private String nextPageUrl;
    @JsonProperty("prev_page_url")
    private String prevPageUrl;
    private int from;
    private int to;
    private List<TransactionListDataResponse> data;
}
