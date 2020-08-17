package net.digitallogic.aclUidTest.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RoleEntity")
@Table(name = "role_entity")
public class RoleEntity extends UUID_ID {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_authorities",
            joinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="authority_id", referencedColumnName = "id")
    )
    private Set<AuthorityEntity> authorities;

    public void addAuthority(AuthorityEntity authorityEntity) {
        authorities.add(authorityEntity);
    }
    public void removeAuthority(AuthorityEntity authorityEntity) {
        authorities.remove(authorityEntity);
    }
}
