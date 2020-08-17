package net.digitallogic.aclUidTest.persistence.repository;

import net.digitallogic.aclUidTest.persistence.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, UUID> {
}
