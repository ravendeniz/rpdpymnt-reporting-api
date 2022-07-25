package com.rpdpymnt.reporting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
public enum Role {
    ADMINISTRATOR("ROLE_ADMINISTRATOR", 1 << 0),
    SERVICE("ROLE_SERVICE", 1 << 1);

    private static final long MIN_VALUE = 0;
    private static final long MAX_VALUE = (long) (Math.pow(2, Role.values().length) - 1);

    @Getter
    private final String name;

    @Getter
    private final long value;

    public static Role[] allFromLong(long userRoleValue) {
        if (!(userRoleValue >= MIN_VALUE && userRoleValue <= MAX_VALUE)) {
            throw new IllegalArgumentException(String.format("User role values {%d} out of margin", userRoleValue));
        }

        return Stream.of(Role.values()).filter(role -> userHasRole(role.getValue(), userRoleValue))
                .toArray(Role[]::new);
    }

    private static boolean userHasRole(long roleValue, long userRoleValue) {
        return (roleValue & userRoleValue) != 0;
    }

    public static boolean userHasAdministratorRole(long checkedRoleValue) {
        return userHasRole(checkedRoleValue, ADMINISTRATOR.getValue());
    }

    public static boolean userHasServiceRole(long checkedRoleValue) {
        return userHasRole(checkedRoleValue, SERVICE.getValue());
    }

    public static long grantRoleToUser(long userRoleValue, long grantingRoleValue) {
        return userRoleValue | grantingRoleValue;
    }

    public static long revokeRoleFromUser(long userRoleValue, long revokingRoleValue) {
        if (userHasRole(userRoleValue, revokingRoleValue)) {
            return userRoleValue ^ revokingRoleValue;
        } else {
            return userRoleValue;
        }
    }

    public static List<GrantedAuthority> getAuthorities(long roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (userHasAdministratorRole(roles)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(ADMINISTRATOR.getName()));
        }
        return grantedAuthorities;
    }

}
