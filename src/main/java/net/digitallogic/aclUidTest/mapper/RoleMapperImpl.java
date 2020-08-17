package net.digitallogic.aclUidTest.mapper;

import net.digitallogic.aclUidTest.persistence.entity.RoleEntity;
import net.digitallogic.aclUidTest.security.Authority;
import net.digitallogic.aclUidTest.web.dto.RoleDto;
import net.digitallogic.aclUidTest.web.response.RoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class RoleMapperImpl implements RoleMapper {
    private final AuthorityMapper authorityMapper;

    @Autowired
    public RoleMapperImpl(AuthorityMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }

    @Override
    public RoleEntity toEntity(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        RoleEntity.RoleEntityBuilder<?, ?> roleEntity = RoleEntity.builder();

        roleEntity.id( roleDto.getId() );
        roleEntity.version( roleDto.getVersion() );
        roleEntity.name( roleDto.getName() );

        return roleEntity.build();
    }

    @Override
    public RoleDto toDto(RoleEntity roleEntity) {
        if ( roleEntity == null ) {
            return null;
        }

        RoleDto.RoleDtoBuilder roleDto = RoleDto.builder();

        roleDto.id( roleEntity.getId() );
        roleDto.name( roleEntity.getName() );

        // If authorities are loaded then we will map them.
        PersistenceUtil pu = Persistence.getPersistenceUtil();
        if (pu.isLoaded(roleEntity.getAuthorities())) {
            roleDto.authorities(authorityMapper.toDto(roleEntity.getAuthorities()));
        }

        roleDto.version( roleEntity.getVersion() );

        return roleDto.build();
    }

    @Override
    public List<RoleDto> toDto(Iterable<RoleEntity> roleEntities) {
        if ( roleEntities == null ) {
            return null;
        }

        List<RoleDto> list = new ArrayList<RoleDto>();
        for ( RoleEntity roleEntity : roleEntities ) {
            list.add( toDto( roleEntity ) );
        }

        return list;
    }

    @Override
    public RoleResponse toResponse(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.id( roleDto.getId() );
        roleResponse.name( roleDto.getName() );
        roleResponse.authorities(
                authorityMapper.toResponse( roleDto.getAuthorities() )
        );

        roleResponse.version( roleDto.getVersion() );

        return roleResponse.build();
    }

    @Override
    public List<RoleResponse> toResponse(Iterable<RoleDto> roleDtos) {
        if (roleDtos == null)
            return null;

        return StreamSupport.stream(roleDtos.spliterator(), false)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Authority> toAuthority(Iterable<RoleEntity> roleEntities) {
        return StreamSupport.stream(roleEntities.spliterator(), false)
                .map(RoleEntity::getAuthorities)
                .flatMap(Collection::stream)
                .map(authorityMapper::toAuthority)
                .collect(Collectors.toSet());
    }
}
