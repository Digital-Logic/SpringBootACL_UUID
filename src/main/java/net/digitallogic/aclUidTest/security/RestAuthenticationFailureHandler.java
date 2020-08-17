package net.digitallogic.aclUidTest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.digitallogic.aclUidTest.web.response.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        // remove accessToken, if one exists.
        Cookie cookie = WebUtils.getCookie(request, SecurityConstants.ACCESS_TOKEN);
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setValue("");
            response.addCookie(cookie);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        new ObjectMapper().writeValue(response.getWriter(),
                ErrorResponse.builder()
                        .message(exception.getMessage())
                        .build()
        );
    }
}
