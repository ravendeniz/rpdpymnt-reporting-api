package com.rpdpymnt.reporting.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;

import com.rpdpymnt.reporting.model.Role;
import com.rpdpymnt.reporting.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private static final String BCRYPT_ALG_2Y = "$2y$";
    private static final String BCRYPT_ALG_2A = "$2a$";

    private Long id;

    private String username;

    private String name;

    private String lastName;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(UserProfile user) {
        List<GrantedAuthority> authorities = Role.getAuthorities(user.getRoles());

        // CVLGN-31 hash algoritm, after upgrading spring security 5.2.1
        // should be deleted
        String password = user.getPassword().startsWith(BCRYPT_ALG_2Y)
                ? user.getPassword().replace(BCRYPT_ALG_2Y, BCRYPT_ALG_2A)
                : user.getPassword();

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getName(),
                user.getEmail(),
                password,
                authorities
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
