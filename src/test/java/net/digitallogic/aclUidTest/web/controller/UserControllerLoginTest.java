package net.digitallogic.aclUidTest.web.controller;

import net.digitallogic.aclUidTest.config.Profiles;
import net.digitallogic.aclUidTest.fixtures.UserFixtures;
import net.digitallogic.aclUidTest.persistence.repository.UserRepository;
import net.digitallogic.aclUidTest.web.request.CreateUserRequest;
import net.digitallogic.aclUidTest.web.response.UserResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles({Profiles.H2, Profiles.TESTING})
@Disabled
public class UserControllerLoginTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithAnonymousUser
    public void createUserTest() {
        CreateUserRequest createUser = UserFixtures.getCreateUserRequest();
        UserResponse response = userController.signUserUp(createUser);
        assertThat(response).isNotNull();
    }
}
