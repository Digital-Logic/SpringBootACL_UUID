package net.digitallogic.aclUidTest.web.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {

    private UUID id;
    private String email;
    private String password;

    @Builder.Default
    private List<RoleDto> roles = new ArrayList<>();
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean accountEnabled;
    private boolean credentialsExpired;
    private int version;
}
