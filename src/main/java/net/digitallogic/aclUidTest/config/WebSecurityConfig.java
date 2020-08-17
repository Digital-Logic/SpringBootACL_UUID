package net.digitallogic.aclUidTest.config;

import net.digitallogic.aclUidTest.mapper.UserMapper;
import net.digitallogic.aclUidTest.persistence.repository.UserRepository;
import net.digitallogic.aclUidTest.security.*;
import net.digitallogic.aclUidTest.services.UserService;
import net.digitallogic.aclUidTest.web.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String accessTokenSecret;
    private final long accessTokenExpiration;
    private final String tokenIss;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public WebSecurityConfig(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            UserService userService,
            @Value("${token.access.secret}") String accessTokenSecret,
            @Value("${token.access.expires}") long accessTokenExpiration,
            @Value("${token.iss}") String tokenIss,
            UserRepository userRepository,
            UserMapper userMapper) throws Exception {

        this.accessTokenSecret = accessTokenSecret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.tokenIss = tokenIss;
        this.userRepository = userRepository;
        this.userMapper = userMapper;

        authenticationManagerBuilder.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                    .disable()

                .csrf()
                    .disable()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                .exceptionHandling()
                    .accessDeniedHandler(unauthorizedHandler())
                    .and()

                .addFilterAt(new SignInFilter(authenticationManager(), authenticationSuccessHandler(), authenticationFailureHandler()),
                        UsernamePasswordAuthenticationFilter.class)

                .addFilterAfter(new TokenProcessingFilter(accessTokenGenerator(), userRepository, userMapper),
                        UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, Routes.SIGN_IN_ROUTE).permitAll()
                    .antMatchers(HttpMethod.POST, Routes.SIGN_UP_ROUTE).permitAll()

                .anyRequest()
                    .authenticated()
        ;
    }

    @Bean
    @AccessToken
    public TokenGenerator accessTokenGenerator() {
        return new TokenGenerator(accessTokenSecret, tokenIss, accessTokenExpiration);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler(accessTokenGenerator());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler unauthorizedHandler() {
        return new UnauthorizedHandler();
    }
}
