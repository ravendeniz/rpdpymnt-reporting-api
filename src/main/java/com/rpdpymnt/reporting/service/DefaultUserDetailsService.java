package com.rpdpymnt.reporting.service;

import com.rpdpymnt.reporting.model.UserProfile;
import com.rpdpymnt.reporting.repository.UserProfileRepository;
import com.rpdpymnt.reporting.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    public DefaultUserDetailsService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with username or email : " + email));
        return UserPrincipal.create(user);
    }
}