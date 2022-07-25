package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.UserRoleInfo;
import com.rpdpymnt.reporting.model.UserProfile;
import com.rpdpymnt.reporting.repository.UserProfileRepository;
import com.rpdpymnt.reporting.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DefaultTokenService implements TokenService {

    private static final int TOKEN_LENGTH = 128;
    private static final String NO_AUTHENTICATION_TOKEN_FOUND = "No authentication token found";
    private static final String ACCESS_TOKEN_IS_INVALID = "Access token is invalid";

    private final JwtTokenProvider tokenProvider;
    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;

    public DefaultTokenService(JwtTokenProvider tokenProvider,
                               UserProfileRepository userProfileRepository,
                               AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.userProfileRepository = userProfileRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Optional<UserRoleInfo> validate(String token) {
        if (StringUtils.isBlank(token)) {
            throw new SecurityException(NO_AUTHENTICATION_TOKEN_FOUND);
        }
        final boolean authenticated = tokenProvider.validateToken(token);
        if (!authenticated) {
            throw new SecurityException(ACCESS_TOKEN_IS_INVALID);
        }
        final long id = tokenProvider.getUserIdFromJwt(token);
        final Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        if (userProfile.isPresent()) {
            return getUserRoleInfo(userProfile.get());
        }
        return Optional.empty();
    }

    private Optional<UserRoleInfo> getUserRoleInfo(UserProfile userProfile) {
        final UserRoleInfo info = UserRoleInfo.builder()
                .userId(userProfile.getId())
                .roles(userProfile.getRoles())
                .lastLoginTimestamp(System.currentTimeMillis())
                .build();
        return Optional.of(info);
    }

    @Override
    public String generateToken(String email, String password) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        return tokenProvider.generateToken(authentication);
    }

    @Override
    public UserProfile getUserProfile(String token) {
        final boolean authenticated = tokenProvider.validateToken(token);
        if (!authenticated) {
            throw new SecurityException(ACCESS_TOKEN_IS_INVALID);
        }
        final long id = tokenProvider.getUserIdFromJwt(token);
        final Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        if (userProfile.isPresent()) {
            return userProfile.get();
        }
        return null;
    }
}
