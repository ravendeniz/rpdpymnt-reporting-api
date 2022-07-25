package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.model.UserProfile;

public interface UserService {

    boolean isUserExist(String email);

    UserProfile getUserProfileByEmail(String email);
}
