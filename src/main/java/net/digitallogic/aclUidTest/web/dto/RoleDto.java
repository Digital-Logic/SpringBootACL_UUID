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
public class RoleDto {

    private UUID id;
    private String name;

    @Builder.Default
    private List<AuthorityDto> authorities = new ArrayList<>();
    private int version;
}
