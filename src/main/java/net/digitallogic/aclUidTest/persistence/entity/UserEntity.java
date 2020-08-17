package net.digitallogic.aclUidTest.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "UserEntity")
@Table(name = "user_entity")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserEntity extends UUID_ID {

    @Column(nullable = false, unique = true, length = 120)
    private String email;
    @Column(nullable = false, unique = true)
    private String encryptedPassword;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public void addRole(RoleEntity roleEntity) {
        roles.add(roleEntity);
    }

    public void removeRole(RoleEntity roleEntity) {
        roles.remove(roleEntity);
    }

    @Column(nullable = false)
    private boolean accountExpired;

    @Column(nullable = false)
    private boolean accountLocked;

    @Column(nullable = false)
    private boolean accountEnabled;

    @Column(nullable = false)
    private boolean credentialsExpired;
}
