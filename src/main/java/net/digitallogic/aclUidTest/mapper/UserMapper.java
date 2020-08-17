package net.digitallogic.aclUidTest.mapper;

import net.digitallogic.aclUidTest.persistence.entity.UserEntity;
import net.digitallogic.aclUidTest.security.UserPrincipal;
import net.digitallogic.aclUidTest.web.dto.UserDto;
import net.digitallogic.aclUidTest.web.request.CreateUserRequest;
import net.digitallogic.aclUidTest.web.response.UserResponse;

import java.util.List;

//@Mapper(
//        config = MapperConfig.class,
//        uses = { RoleMapper.class }
//)
public interface UserMapper {

//    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserDto userDto);

    UserDto toDto(CreateUserRequest createUserRequest);

  //  @Mapping(target = "password", source = "encryptedPassword")
    UserPrincipal toPrincipal(UserEntity userEntity);

    UserDto toDto(UserEntity userEntity);
    List<UserDto> toDto(Iterable<UserEntity> userEntities);

    UserResponse toResponse(UserDto userDto);
    List<UserResponse> toResponse(Iterable<UserDto> userDtos);
    UserResponse toResponse(UserPrincipal userPrincipal);
}
