package net.digitallogic.aclUidTest.persistence.repository;

import net.digitallogic.aclUidTest.persistence.entity.AuthorityEntity;
import net.digitallogic.aclUidTest.security.Authorities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthoritiesRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void validateAuthoritiesTest() {
        List<AuthorityEntity> authorityEntities = StreamSupport.stream(authorityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertThat(authorityEntities).hasSize(Authorities.values().length);
    }
}
