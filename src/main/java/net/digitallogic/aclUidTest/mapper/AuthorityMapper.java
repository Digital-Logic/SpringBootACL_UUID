package net.digitallogic.aclUidTest.mapper;

import net.digitallogic.aclUidTest.persistence.entity.AuthorityEntity;
import net.digitallogic.aclUidTest.security.Authority;
import net.digitallogic.aclUidTest.web.dto.AuthorityDto;
import net.digitallogic.aclUidTest.web.response.AuthorityResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(
        config = MapperConfig.class
)
public interface AuthorityMapper {

    AuthorityEntity toEntity(AuthorityDto authorityDto);

    AuthorityDto toDto(AuthorityEntity authorityEntity);
    List<AuthorityDto> toDto(Iterable<AuthorityEntity> authorityEntities);

    AuthorityResponse toResponse(AuthorityDto authorityDto);
    List<AuthorityResponse> toResponse(Iterable<AuthorityDto> authorityDtos);

    Authority toAuthority(AuthorityEntity authorityEntity);
    Set<Authority> toAuthority(Iterable<AuthorityEntity> authorityEntities);


    String toString(Authority authority);
    List<String> toString(Iterable<Authority> authorities);
}
