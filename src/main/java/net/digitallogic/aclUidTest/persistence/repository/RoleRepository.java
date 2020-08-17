package net.digitallogic.aclUidTest.persistence.repository;

import net.digitallogic.aclUidTest.persistence.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends CrudRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByName(String name);
}
