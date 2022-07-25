package com.rpdpymnt.reporting.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginResponse {

    @NotBlank
    private String email;
    private String userName;
    private long roles;
    @NotBlank
    private String accessToken;
    @NotBlank
    @Builder.Default
    private String tokenType = "Bearer";
}
