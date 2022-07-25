package com.rpdpymnt.reporting.config;

import com.rpdpymnt.reporting.security.SecurityFilter;
import com.rpdpymnt.reporting.service.DefaultUserDetailsService;
import com.rpdpymnt.reporting.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("PMD.SignatureDeclareThrowsException")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {"/swagger-resources/**", "/swagger-ui.html",
            "/v2/api-docs", "/webjars/**", "/api/**"};

    @Autowired
    DefaultUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "securityFilter")
    public SecurityFilter securityFilterToken(TokenService tokenService) {
        final String[] excludedUrls = {
                "/api/users/login",
                "/api/transactions/report",
                "/api/transaction/list",
                "/api/transaction",
                "/api/client"};
        return new SecurityFilter(tokenService, excludedUrls);
    }

    @Bean
    public FilterRegistrationBean appSecurityFilter(SecurityFilter securityFilter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Integer.MAX_VALUE);
        registrationBean.setFilter(securityFilter);
        return registrationBean;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .disable()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

}
