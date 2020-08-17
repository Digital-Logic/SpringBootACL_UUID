package net.digitallogic.aclUidTest.mapper;

import net.digitallogic.aclUidTest.persistence.entity.UserEntity;
import net.digitallogic.aclUidTest.security.UserPrincipal;
import net.digitallogic.aclUidTest.web.dto.UserDto;
import net.digitallogic.aclUidTest.web.request.CreateUserRequest;
import net.digitallogic.aclUidTest.web.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserMapperImpl implements UserMapper {

    private final RoleMapper roleMapper;

    private final AuthorityMapper authorityMapper;

    @Autowired
    public UserMapperImpl(RoleMapper roleMapper, AuthorityMapper authorityMapper) {
        this.roleMapper = roleMapper;
        this.authorityMapper = authorityMapper;
    }

    @Override
    public UserEntity toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        UserEntity.UserEntityBuilder<?, ?> userEntity = UserEntity.builder();

        userEntity.id(userDto.getId());
        userEntity.version(userDto.getVersion());
        userEntity.email(userDto.getEmail());
        userEntity.accountExpired(userDto.isAccountExpired());
        userEntity.accountLocked(userDto.isAccountLocked());
        userEntity.accountEnabled(userDto.isAccountEnabled());
        userEntity.credentialsExpired(userDto.isCredentialsExpired());

        return userEntity.build();
    }

    @Override
    public UserDto toDto(CreateUserRequest createUserRequest) {
        if (createUserRequest == null) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.email(createUserRequest.getEmail());
        userDto.password(createUserRequest.getPassword());

        return userDto.build();
    }

    @Override
    public UserPrincipal toPrincipal(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        UserPrincipal.UserPrincipalBuilder userPrincipal = UserPrincipal.builder();

        userPrincipal.password(userEntity.getEncryptedPassword());
        userPrincipal.id(userEntity.getId());
        userPrincipal.email(userEntity.getEmail());
        userPrincipal.accountExpired(userEntity.isAccountExpired());
        userPrincipal.accountLocked(userEntity.isAccountLocked());
        userPrincipal.accountEnabled(userEntity.isAccountEnabled());
        userPrincipal.credentialsExpired(userEntity.isCredentialsExpired());

        return userPrincipal.build();
    }

    @Override
    public UserDto toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id(userEntity.getId());
        userDto.email(userEntity.getEmail());

        PersistenceUtil pu = Persistence.getPersistenceUtil();
        if (pu.isLoaded(userEntity.getRoles())) {
            userDto.roles(roleMapper.toDto(userEntity.getRoles()));
        }

        userDto.accountExpired(userEntity.isAccountExpired());
        userDto.accountLocked(userEntity.isAccountLocked());
        userDto.accountEnabled(userEntity.isAccountEnabled());
        userDto.credentialsExpired(userEntity.isCredentialsExpired());
        userDto.version(userEntity.getVersion());

        return userDto.build();
    }

    @Override
    public List<UserDto> toDto(Iterable<UserEntity> userEntities) {
        if (userEntities == null) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>();
        for (UserEntity userEntity : userEntities) {
            list.add(toDto(userEntity));
        }

        return list;
    }

    @Override
    public UserResponse toResponse(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id(userDto.getId());
        userResponse.email(userDto.getEmail());
        userResponse.roles(roleMapper.toResponse(userDto.getRoles()));
        userResponse.accountExpired(userDto.isAccountExpired());
        userResponse.accountLocked(userDto.isAccountLocked());
        userResponse.accountEnabled(userDto.isAccountEnabled());
        userResponse.credentialsExpired(userDto.isCredentialsExpired());

        return userResponse.build();
    }

    @Override
    public List<UserResponse> toResponse(Iterable<UserDto> userDtos) {
        if (userDtos == null) {
            return null;
        }

        return StreamSupport.stream(userDtos.spliterator(), false)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse toResponse(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return null;
        }
        return UserResponse.builder()
                .id(userPrincipal.getId())
                .email(userPrincipal.getUsername())
                .accountEnabled(userPrincipal.isEnabled())
                .accountExpired(!userPrincipal.isAccountNonExpired())
                .accountLocked(!userPrincipal.isAccountNonLocked())
                .credentialsExpired(!userPrincipal.isCredentialsNonExpired())
                .authorities(authorityMapper.toString(userPrincipal.getAuthorities()))
                .build();
    }
}
