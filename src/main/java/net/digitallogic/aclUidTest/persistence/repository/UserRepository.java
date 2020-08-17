package net.digitallogic.aclUidTest.persistence.repository;

import net.digitallogic.aclUidTest.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH r.authorities a " +
            "WHERE LOWER(u.email) = LOWER(:email)")
    Optional<UserEntity> findByEmailWithAuthorities(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE u.id = :id")
    Optional<UserEntity> findByIdWithRoles(@Param("id") UUID id);

    @Query("SELECT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH r.authorities a " +
            "WHERE u.id = :id")
    Optional<UserEntity> findByIdWithAuthorities(@Param("id") UUID id);
}
