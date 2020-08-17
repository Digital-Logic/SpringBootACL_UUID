package net.digitallogic.aclUidTest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.digitallogic.aclUidTest.web.Routes;
import net.digitallogic.aclUidTest.web.request.SignInRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SignInFilter extends AbstractAuthenticationProcessingFilter {


    public SignInFilter(AuthenticationManager authenticationManager,
                        AuthenticationSuccessHandler authenticationSuccessHandler,
                        AuthenticationFailureHandler authenticationFailureHandler) {

        super(new AntPathRequestMatcher(Routes.SIGN_IN_ROUTE, HttpMethod.POST.name()));

        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        SignInRequest signInRequest = new ObjectMapper().readValue(request.getInputStream(), SignInRequest.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        // Continue the request to the controller, this will allow the controller to generate the response.
        chain.doFilter(request, response);
    }
}
