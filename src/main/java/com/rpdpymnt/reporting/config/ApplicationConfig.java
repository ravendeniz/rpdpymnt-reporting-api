package com.rpdpymnt.reporting.config;

import com.rpdpymnt.reporting.RpdpymntProperties;
import com.rpdpymnt.reporting.settings.JwtSettings;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = {"com.rpdpymnt.reporting"})
@EnableConfigurationProperties(RpdpymntProperties.class)
public class ApplicationConfig implements WebMvcConfigurer {

    private final RpdpymntProperties rpdpymntProperties;

    public ApplicationConfig(RpdpymntProperties rpdpymntProperties) {
        this.rpdpymntProperties = rpdpymntProperties;
    }

    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }

    @Bean
    public JwtSettings jwtSettings() {
        return rpdpymntProperties.getJwt();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
}

