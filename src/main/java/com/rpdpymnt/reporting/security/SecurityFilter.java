package com.rpdpymnt.reporting.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rpdpymnt.reporting.dto.UserRoleInfo;
import com.rpdpymnt.reporting.model.Role;
import com.rpdpymnt.reporting.service.TokenService;
import com.rpdpymnt.reporting.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "authorization";
    private static final String EXCLUDE_WILDCARD = "*";
    private static final String REGEX_URL_WILDCARD = "^(/?(.*))(%s)(/?(.*))$";
    private static final String INVALID_TOKEN = "Access token is invalid";
    private static final String EMPTY_TOKEN = "No authentication token found";

    private final TokenService tokenService;
    private final String[] excludedUrls;

    public SecurityFilter(TokenService tokenServiceAuth, final String[] excludedUrls) {
        this.tokenService = tokenServiceAuth;
        this.excludedUrls = excludedUrls.clone();
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (prepareInternalFilter(request, response, filterChain)) {
            return;
        }
        try {
            String token = request.getHeader(AUTHORIZATION);
            if (StringUtils.isEmpty(token)) {
                createUnauthorizedResponse(response, new SecurityException(EMPTY_TOKEN));
            } else {
                UserRoleInfo userProfile = tokenService.validate(token)
                        .orElseThrow(() -> new SecurityException("No user found for that token"));
                createAuthentication(userProfile.getRoles());
                final Long userId = userProfile.getUserId();
                filterChain.doFilter(new CustomServletRequestWrapper(request, String.valueOf(userId)), response);
            }
        } catch (SecurityException ex) {
            createResponse(response, ex);
        } catch (RuntimeException exception) {
            createGenericResponse(response, exception);
        }
    }

    private boolean prepareInternalFilter(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain filterChain) throws IOException, ServletException {

        if (isNonSecureUrl(request.getRequestURL().toString())) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private void createResponse(HttpServletResponse response, SecurityException ex) throws IOException {
        if (ex.getMessage().contains(INVALID_TOKEN)) {
            createUnauthorizedResponse(response, ex);
        } else {
            createSecurityResponse(response, ex);
        }
    }

    private void createSecurityResponse(HttpServletResponse response, SecurityException ex) throws IOException {
        SecurityContextHolder.clearContext();
        PrintWriter out = response.getWriter();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ResponseUtil.sendError(HttpStatus.FORBIDDEN.value(), ex, out);
        log.error("Error authenticating user: {}", getMessageDetails(ex));
    }

    private void createUnauthorizedResponse(HttpServletResponse response, SecurityException ex) throws IOException {
        SecurityContextHolder.clearContext();
        PrintWriter out = response.getWriter();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseUtil.sendError(HttpStatus.UNAUTHORIZED.value(), ex, out);
        log.error("Error authenticating user: {}", getMessageDetails(ex));
    }

    private String getMessageDetails(SecurityException exc) {
        Throwable cause = exc.getCause();
        return exc.getMessage() + (cause == null ? "" : ". Cause: " + cause.getMessage());
    }

    private void createGenericResponse(HttpServletResponse response, Exception exception) throws IOException {
        SecurityContextHolder.clearContext();
        log.error("Error on token authentication", exception);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        PrintWriter out = response.getWriter();
        ResponseUtil.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception, out);
    }

    private void createAuthentication(long userRoleValue) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Role[] roles = Role.allFromLong(userRoleValue);
            List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
            for (Role role : roles) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                updatedAuthorities.add(authority);
            }
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                            authentication.getCredentials(), updatedAuthorities));
        }
    }

    private boolean isNonSecureUrl(String url) {
        for (String excludedUrl : excludedUrls) {
            if (!StringUtils.isEmpty(excludedUrl)) {
                if (excludedUrl.endsWith(EXCLUDE_WILDCARD)) {
                    final String regex = String.format(REGEX_URL_WILDCARD, excludedUrl.replace(EXCLUDE_WILDCARD, ""));
                    if (url.matches(regex)) {
                        return true;
                    }
                } else {
                    if (url.endsWith(excludedUrl)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
