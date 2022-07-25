package com.rpdpymnt.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class BadRequestDetails {

    private String details;
}
