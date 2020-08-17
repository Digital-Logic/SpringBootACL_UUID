package net.digitallogic.aclUidTest.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.aclUidTest.mapper.UserMapper;
import net.digitallogic.aclUidTest.security.SecurityConstants;
import net.digitallogic.aclUidTest.security.UserPrincipal;
import net.digitallogic.aclUidTest.services.UserService;
import net.digitallogic.aclUidTest.web.Routes;
import net.digitallogic.aclUidTest.web.request.CreateUserRequest;
import net.digitallogic.aclUidTest.web.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = Routes.USERS_ROUTE)
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN_USERS_AUTHORITY')")
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userMapper.toResponse(
            userService.createUser(userMapper.toDto(createUserRequest))
        );
    }

    @PostMapping(path = Routes.SIGN_UP)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signUserUp(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userMapper.toResponse(
            userService.createUser(userMapper.toDto(createUserRequest))
        );
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN_USERS_AUTHORITY') or " +
            "hasPermission(#id, 'net.digitallogic.aclUidTest.persistence.entity.UserEntity', 'read')")
    public UserResponse getUser(@PathVariable("id") UUID id) {
        return userMapper.toResponse(
            userService.getUser(id)
        );
    }

    @PostMapping(path = Routes.SIGN_IN)
    @ResponseStatus(HttpStatus.OK)
    public UserResponse signIn(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userMapper.toResponse(userPrincipal);
    }

    @RequestMapping(path = Routes.SIGN_OUT)
    public void signOut(HttpServletRequest request,
                                  HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, SecurityConstants.ACCESS_TOKEN);
        cookie.setMaxAge(0);
        cookie.setValue("");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN_USERS_AUTHORITY')")
    public List<UserResponse> getUsers() {
        return userMapper.toResponse(
            userService.getUsers()
        );
    }
}
