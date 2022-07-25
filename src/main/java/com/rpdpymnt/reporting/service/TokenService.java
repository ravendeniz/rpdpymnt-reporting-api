package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.dto.UserRoleInfo;
import com.rpdpymnt.reporting.model.UserProfile;

import java.util.Optional;

public interface TokenService {

    Optional<UserRoleInfo> validate(String token);

    String generateToken(String email, String password);

    UserProfile getUserProfile(String token);
}
