package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.model.UserProfile;
import com.rpdpymnt.reporting.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private static final String EMAIL_ALREADY_EXIST = "A user with the specified email already exist.";
    private static final String USER_NON_EXIST = "A user with the specified token does not exist.";

    private final UserProfileRepository userProfileRepository;

    public boolean isUserExist(String email) {
        Optional<UserProfile> existingUser = userProfileRepository.findByEmail(email);
        return existingUser.isPresent();
    }

    @Override
    public UserProfile getUserProfileByEmail(String email) {
        return userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email : " + email));
    }
}
