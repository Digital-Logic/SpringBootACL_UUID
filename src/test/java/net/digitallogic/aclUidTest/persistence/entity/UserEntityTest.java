package net.digitallogic.aclUidTest.persistence.entity;

import net.digitallogic.aclUidTest.fixtures.UserFixtures;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    @Test
    public void equalityAndHashTest() {
        UserEntity user = UserFixtures.getUserEntity();
        UserEntity copy = UserEntity.builder().id(user.getId()).build();

        assertThat(user).isEqualTo(copy);
        assertThat(user).hasSameHashCodeAs(copy);
    }
}
