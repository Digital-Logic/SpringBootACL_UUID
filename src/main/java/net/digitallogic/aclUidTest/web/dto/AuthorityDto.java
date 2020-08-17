package net.digitallogic.aclUidTest.web.dto;

import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AuthorityDto {

    private UUID id;
    private String name;
    private int version;
}
