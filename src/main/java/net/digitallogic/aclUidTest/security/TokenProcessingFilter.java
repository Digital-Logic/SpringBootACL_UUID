package net.digitallogic.aclUidTest.security;

import net.digitallogic.aclUidTest.mapper.UserMapper;
import net.digitallogic.aclUidTest.persistence.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class TokenProcessingFilter extends OncePerRequestFilter {

    private final TokenGenerator token;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public TokenProcessingFilter(TokenGenerator token,
                                 UserRepository userRepository,
                                 UserMapper userMapper) {
        this.token = token;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = WebUtils.getCookie(request, SecurityConstants.ACCESS_TOKEN);

        if (cookie != null) {
            String tokenString = cookie.getValue();
            UUID userId = UUID.fromString(token.verifyToken(tokenString).getSubject());

            userRepository.findByIdWithAuthorities(userId).ifPresent(userEntity -> {
                UserPrincipal userPrincipal = userMapper.toPrincipal(userEntity);

                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userPrincipal, tokenString, userPrincipal.getAuthorities())
                );
            });
        }

        filterChain.doFilter(request, response);
    }
}
