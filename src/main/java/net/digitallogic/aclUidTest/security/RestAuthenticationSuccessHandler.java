package net.digitallogic.aclUidTest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenGenerator accessTokenGenerator;

    public RestAuthenticationSuccessHandler(TokenGenerator accessTokenGenerator) {
        this.accessTokenGenerator = accessTokenGenerator;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Cookie cookie = new Cookie(SecurityConstants.ACCESS_TOKEN, accessTokenGenerator.allocateToken(userPrincipal.getId().toString()));

        cookie.setMaxAge((int) accessTokenGenerator.getExpiration() / 1000);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
}
