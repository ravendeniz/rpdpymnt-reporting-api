package com.rpdpymnt.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ErrorDetails {

    private int code;

    private String message;
}
