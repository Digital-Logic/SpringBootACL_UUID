package net.digitallogic.aclUidTest.fixtures;

import net.digitallogic.aclUidTest.persistence.entity.UserEntity;
import net.digitallogic.aclUidTest.web.dto.UserDto;
import net.digitallogic.aclUidTest.web.request.CreateUserRequest;
import org.mockito.Mock;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserFixtures {

    @Mock
    private static AuthenticationManagerBuilder authenticationManagerBuilder;

    private static Random random = new Random();

    private static PasswordEncoder encoder = new DelegatingPasswordEncoder("bcrypt", Map.of(
            "bcrypt", new BCryptPasswordEncoder(12)
    ));

    private static List<UserDto> userDtoList = List.of(
            UserDto.builder().id(UUID.randomUUID()).email("Joe@Exotic.com").password("joe_password").build(),
            UserDto.builder().id(UUID.randomUUID()).email("JohnWick@BadAss.com").password("john_password").build(),
            UserDto.builder().id(UUID.randomUUID()).email("sarah@conner.com").password("sarah_password").build(),
            UserDto.builder().id(UUID.randomUUID()).email("bob@barker.com").password("bobbys_password").build(),
            UserDto.builder().id(UUID.randomUUID()).email("howard@theDuck.com").password("stupidStupidMovie").build()
    );
    private static Map<String, UserDto> userDtoMap = userDtoList.stream()
            .collect(Collectors.toMap(UserDto::getEmail, Function.identity()));

    private static List<UserEntity> userEntityList = userDtoList.stream()
            .map(userDto -> UserEntity.builder()
                    .id(userDto.getId())
                    .email(userDto.getEmail())
                    .encryptedPassword(encoder.encode(userDto.getPassword()))
                    .build()
            )
            .collect(Collectors.toList());
    private static Map<String, UserEntity> userEntityMap = userEntityList.stream()
            .collect(Collectors.toMap(UserEntity::getEmail, Function.identity()));

    private static List<CreateUserRequest> createUserRequestList = userDtoList.stream()
            .map(userDto -> CreateUserRequest.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build()
            ).collect(Collectors.toList());
    private static Map<String, CreateUserRequest> createUserRequestMap = createUserRequestList.stream()
            .collect(Collectors.toMap(CreateUserRequest::getEmail, Function.identity()));

    /* ********* Copy Methods ************ */
    private static UserDto copy(UserDto user) {
        return user.toBuilder().build();
    }

    private static UserEntity copy(UserEntity userEntity) {
        return userEntity.toBuilder().build();
    }

    private static CreateUserRequest copy(CreateUserRequest userRequest) {
        return userRequest.toBuilder().build();
    }

    /* *********** Get Signal UserDto ************ */
    public static UserDto.UserDtoBuilder getUserDtoBuilder() {
        return userDtoList.get(random.nextInt(userDtoList.size()))
                .toBuilder();
    }

    public static UserDto getUserDto() {
        return copy(userDtoList.get(random.nextInt(userDtoList.size())));
    }

    public static CreateUserRequest getCreateUserRequest() {
        return copy(createUserRequestList.get(random.nextInt(createUserRequestList.size())));
    }

    /* ************** Get User By Email ******************* */
    public static UserEntity getUserEntity(String email) {
        return copy(
                userEntityMap.get(email)
        );
    }
    public static UserDto getUserDto(String email) {
        return copy(
                userDtoMap.get(email)
        );
    }

    public static CreateUserRequest getCreateUserRequest(String email) {
        return copy(
                createUserRequestMap.get(email)
        );
    }


    /* *********** Get List of UserDto *********** */
    public static List<UserDto.UserDtoBuilder> getUserDtoBuilderList() { return getUserDtoBuilderList(userDtoList.size()); }
    public static List<UserDto.UserDtoBuilder> getUserDtoBuilderList(int size) {
        return userDtoList.stream()
                .limit(size)
                .map(UserDto::toBuilder)
                .collect(Collectors.toList());
    }

    private static List<UserDto> getUserDtoList() {
        return getUserDtoList(userDtoList.size());
    }

    private static List<UserDto> getUserDtoList(int size) {
        return userDtoList.stream()
                .limit(size)
                .map(UserFixtures::copy)    // Duplicate items
                .collect(Collectors.toList());
    }

    /* *********** Get List of CreateUserRequest *********** */
    public static List<CreateUserRequest.CreateUserRequestBuilder> getCreateUserRequestBuilderList() { return getCreateUserRequestBuilderList(createUserRequestList.size()); }
    public static List<CreateUserRequest.CreateUserRequestBuilder> getCreateUserRequestBuilderList(int size) {
        return createUserRequestList.stream()
                .limit(size)
                .map(CreateUserRequest::toBuilder)
                .collect(Collectors.toList());
    }

    private static List<CreateUserRequest> getCreateUserRequestList() {
        return getCreateUserRequestList(createUserRequestList.size());
    }

    private static List<CreateUserRequest> getCreateUserRequestList(int size) {
        return createUserRequestList.stream()
                .limit(size)
                .map(UserFixtures::copy)    // Duplicate items
                .collect(Collectors.toList());
    }

    /* ********** Get Single UserEntity ************ */
    public static UserEntity.UserEntityBuilder getUserEntityBuilder() {
        return userEntityList.get(random.nextInt(userEntityList.size()))
                .toBuilder();
    }

    public static UserEntity getUserEntity() {
        return copy(userEntityList.get(random.nextInt(userEntityList.size())));
    }

    /* ********** Get List of UserEntity items ************** */
    public static List<UserEntity.UserEntityBuilder> getUserEntityBuilderList() { return getUserEntityBuilderList(userEntityList.size()); }
    public static List<UserEntity.UserEntityBuilder> getUserEntityBuilderList(int size) {
        return userEntityList.stream()
                .limit(size)
                .map(UserEntity::toBuilder)
                .collect(Collectors.toList());
    }

    public static List<UserEntity> getUserEntityList() { return getUserEntityList(userEntityList.size()); }
    public static List<UserEntity> getUserEntityList(int size) {
        return userEntityList.stream()
                .limit(size)
                .map(UserFixtures::copy)
                .collect(Collectors.toList());
    }
}
