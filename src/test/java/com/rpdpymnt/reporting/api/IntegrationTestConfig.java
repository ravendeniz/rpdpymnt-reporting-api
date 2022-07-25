package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.RpdpymntProperties;
import com.rpdpymnt.reporting.settings.JwtSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.rpdpymnt")
@EnableConfigurationProperties(RpdpymntProperties.class)
public class IntegrationTestConfig {

    public static final String ADMIN_USER_EMAIL = "adminuser@fujibas.com";
    public static final String NORMAL_USER_EMAIL = "normaluser@fujibas.com";
    public static final String NON_EXIST_USER_EMAIL = "nonexistuser@fujibas.com";
    public static final long ADMIN_USER_ID = 1000L;
    public static final long DELETABLE_USER_ID = 3000L;
    public static final long NORMAL_USER_ID = 2000L;
    public static final String VALID_TOKEN = "validToken";
    public static final String INVALID_TOKEN = "invalidToken";
    public static final String VALID_TOKEN_WITH_ADMIN_ROLE = "validTokenWithAdminRole";
    public static final String VALID_TOKEN_WITH_DELETABLE_USER = "validTokenWithDeletableUser";
    public static final String AUTH_TOKEN_HEADER = "authorization";

    @Autowired
    private RpdpymntProperties rpdpymntProperties;

    @Autowired
    private JwtSettings jwtSettings;

}
