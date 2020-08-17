package net.digitallogic.aclUidTest.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class Authority implements GrantedAuthority {

    @Getter
    private final UUID id;
    private final String name;

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
