package net.digitallogic.aclUidTest.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public abstract class UUID_ID implements Persistable<UUID> {

    @Id
    @Column(name = "id", columnDefinition = "uuid", unique = true, nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private UUID id;

    @Setter(AccessLevel.PROTECTED)
    @Version
    @Builder.Default
    @Column(name = "optlock", nullable = false)
    protected int version = 1;

    @Builder.Default
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void generateId() {
        if (id == null)
            id = UUID.randomUUID();
    }

    // == JPA Hooks == //
    @PrePersist
    void autoGenerateId() { generateId(); }

    @PostPersist
    @PostLoad
    void toggleIsNew() { isNew = false; }
}
