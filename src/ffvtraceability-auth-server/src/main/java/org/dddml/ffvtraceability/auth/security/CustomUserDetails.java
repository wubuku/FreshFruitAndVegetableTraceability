package org.dddml.ffvtraceability.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Set;

public class CustomUserDetails extends User {
    private Set<String> groups;
    private boolean passwordChangeRequired;
    private OffsetDateTime passwordLastChanged;
    private boolean firstLogin;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Set<String> groups, boolean passwordChangeRequired, OffsetDateTime passwordLastChanged, boolean firstLogin) {
        super(username, password, authorities);
        this.groups = groups;
        this.passwordChangeRequired = passwordChangeRequired;
        this.passwordLastChanged = passwordLastChanged;
        this.firstLogin = firstLogin;
    }

    public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public boolean isPasswordExpired() {
        if (passwordLastChanged == null) {
            return true;
        }
        return passwordLastChanged.plusMonths(3)
                .isBefore(OffsetDateTime.now());
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public OffsetDateTime getPasswordLastChanged() {
        return passwordLastChanged;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }
}