package net.digitallogic.aclUidTest.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;


@Builder
@AllArgsConstructor
public final class UserPrincipal implements UserDetails {

    @Getter
    private final UUID id;
    private final String email;
    private final String password;
    private final Set<Authority> authorities;
    private final boolean accountExpired;
    private final boolean accountLocked;
    private final boolean accountEnabled;
    private final boolean credentialsExpired;

    @Override
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return accountEnabled;
    }
}
