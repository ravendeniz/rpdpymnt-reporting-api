package com.rpdpymnt.reporting.settings;

import lombok.Data;

@Data
public class JwtSettings {

    private String secret;
    private long expirationInMs;
    private long expirationVodagrafRegInMs;
}
