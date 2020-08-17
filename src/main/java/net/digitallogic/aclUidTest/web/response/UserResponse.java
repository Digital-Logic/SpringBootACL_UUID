package net.digitallogic.aclUidTest.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse {

    private UUID id;
    private String email;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean accountEnabled;
    private boolean credentialsExpired;

    @Builder.Default
    private List<RoleResponse> roles = new ArrayList<>();

    @Builder.Default
    private List<String> authorities = new ArrayList<>();
}
