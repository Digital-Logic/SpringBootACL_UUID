package net.digitallogic.aclUidTest.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleResponse {

    private UUID id;
    private String name;

    @Builder.Default
    private List<AuthorityResponse> authorities = new ArrayList<>();
    private int version;
}
