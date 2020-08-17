package net.digitallogic.aclUidTest.web.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateUserRequest {

    @Size(max = 100, message = "{error.field.maxLength}")
    @NotNull(message = "{error.field.notNull}")
    private String email;

    @Size(max = 60, message = "{error.field.maxLength}")
    @NotNull(message = "{error.field.notNull}")
    private String password;
}
