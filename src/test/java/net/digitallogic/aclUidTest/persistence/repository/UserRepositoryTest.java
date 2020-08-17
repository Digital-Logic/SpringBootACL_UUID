package net.digitallogic.aclUidTest.persistence.repository;

import net.digitallogic.aclUidTest.persistence.entity.RoleEntity;
import net.digitallogic.aclUidTest.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({"H2", "TEST"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void saveUserEntityTest() {
        UserEntity user = UserEntity.builder()
                .email("joe@exotic.net")
                .encryptedPassword("jlaskdjfl")
                .build();
        UserEntity savedUser = userRepository.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @Sql(value = "classpath:repository/insertUser-h2.sql")
    public void getUserByEmailTest() {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmailIgnoreCase("joe@exotic.com");
        assertThat(userEntityOptional).isNotEmpty();
    }

    @Test
    @Sql(value = "classpath:repository/insertAdminUser-h2.sql")
    public void getUserByIdWithRolesTest() {
        UserEntity userEntity = userRepository.findByEmailIgnoreCase("Sarah@conner.com")
                .orElseThrow();

        UserEntity user = userRepository.findByIdWithRoles(userEntity.getId())
                .orElseThrow();


        assertThat(user.getEmail()).isEqualToIgnoringCase("sarah@conner.com");

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        assertThat(pu.isLoaded(user.getRoles())).describedAs("User roles not loaded").isTrue();

        user.getRoles().forEach(role ->
                assertThat(pu.isLoaded(role.getAuthorities()))
                        .describedAs("Authority loaded when it should not be.")
                        .isFalse()
        );
    }

    @Test
    @Sql(value = "classpath:repository/insertAdminUser-h2.sql")
    public void getUserByIdWithAuthoritiesTest() {
        UserEntity userEntity = userRepository.findByEmailIgnoreCase("Sarah@conner.com")
                .orElseThrow();

        UserEntity user = userRepository.findByIdWithAuthorities(userEntity.getId())
                .orElseThrow();

        RoleEntity adminRole = roleRepository.findByName("ADMIN_ROLE")
                .orElseThrow();

        assertThat(user.getEmail()).isEqualToIgnoringCase("sarah@conner.com");

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        assertThat(pu.isLoaded(user.getRoles())).describedAs("User roles not loaded").isTrue();

        assertThat(user.getRoles()).contains(adminRole);

        user.getRoles().forEach(role ->
                assertThat(pu.isLoaded(role.getAuthorities()))
                        .describedAs("Authority was not loaded.")
                        .isTrue()
        );
    }
}
