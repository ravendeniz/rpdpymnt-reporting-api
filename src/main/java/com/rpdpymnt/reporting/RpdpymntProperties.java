package com.rpdpymnt.reporting;

import com.rpdpymnt.reporting.settings.JwtSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rpdpymnt")
public class RpdpymntProperties {

    private JwtSettings jwt = new JwtSettings();
}
