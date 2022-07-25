package com.rpdpymnt.reporting.repository;

import com.rpdpymnt.reporting.model.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByEmail(String email);

    List<UserProfile> findAllByRoles(long roles);

    List<UserProfile> findAllByRoles(long roles, Pageable pageable);

    List<UserProfile> findAllByRolesNot(long roles);

    List<UserProfile> findAllByRolesNot(long roles, Pageable pageable);
}
