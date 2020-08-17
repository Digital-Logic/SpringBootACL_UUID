package net.digitallogic.aclUidTest.mapper;

import net.digitallogic.aclUidTest.persistence.entity.RoleEntity;
import net.digitallogic.aclUidTest.security.Authority;
import net.digitallogic.aclUidTest.web.dto.RoleDto;
import net.digitallogic.aclUidTest.web.response.RoleResponse;

import java.util.List;
import java.util.Set;

public interface RoleMapper {

    RoleEntity toEntity(RoleDto roleDto);

    RoleDto toDto (RoleEntity roleEntity);
    List<RoleDto> toDto(Iterable<RoleEntity> roleEntities);

    RoleResponse toResponse(RoleDto roleDto);
    List<RoleResponse> toResponse(Iterable<RoleDto> roleDtos);
    Set<Authority> toAuthority(Iterable<RoleEntity> roleEntities);
}
