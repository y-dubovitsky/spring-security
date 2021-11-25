package com.example.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum AppUserRole {
    USER(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
            AppUserPermission.COURSE_READ,
            AppUserPermission.COURSE_WRITE,
            AppUserPermission.STUDENT_READ,
            AppUserPermission.STUDENT_WRITE)
    ),
    TRAINEE_ADMIN(Sets.newHashSet(
            AppUserPermission.COURSE_READ,
            AppUserPermission.STUDENT_READ
    )
    );

    private final Set<AppUserPermission> permissions;

    AppUserRole(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<AppUserPermission> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> grantedAuthoritySet() {
        Set<GrantedAuthority> grantedAuthorities = this.permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return grantedAuthorities;
    }
}
