package net.digitallogic.aclUidTest.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private String path;
    private String message;

    @Builder.Default
    @Setter(AccessLevel.PROTECTED)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date = new Date(System.currentTimeMillis());
}
