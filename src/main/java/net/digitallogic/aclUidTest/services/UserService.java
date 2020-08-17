package net.digitallogic.aclUidTest.services;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.aclUidTest.mapper.UserMapper;
import net.digitallogic.aclUidTest.persistence.entity.UserEntity;
import net.digitallogic.aclUidTest.persistence.repository.UserRepository;
import net.digitallogic.aclUidTest.web.dto.UserDto;
import net.digitallogic.aclUidTest.web.exceptions.BadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final MutableAclService aclService;


    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       MessageSource messageSource,
                       MutableAclService aclService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
        this.aclService = aclService;
    }

    public List<UserDto> getUsers() {
        return userMapper.toDto(
            userRepository.findAll()
        );
    }

    public UserDto getUser(UUID id) {
        return userMapper.toDto(
                userRepository.findById(id)
                    .orElseThrow(() -> new BadRequest(getMessage("exception.entityDoesNotExist", id)))
        );
    }

    @Transactional
    public UserDto createUser(UserDto userDetails) {
        if (userRepository.existsByEmailIgnoreCase(userDetails.getEmail())) {
            throw new BadRequest(getMessage("exception.duplicateUserAccount", userDetails.getEmail()));
        }

        UserEntity userEntity = userMapper.toEntity(userDetails);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
        /* TODO Disabled auto enabled accounts is UserService */
        userEntity.setAccountEnabled(true);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        /* ************** Setup ACL entries ****************** */

        ObjectIdentity oi = new ObjectIdentityImpl(UserEntity.class, savedUserEntity.getId());
        Sid userSid = new PrincipalSid(savedUserEntity.getEmail());
        MutableAcl acl = aclService.createAcl(oi);

        Stream.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE)
                .forEach(permission ->
                        acl.insertAce(acl.getEntries().size(), permission, userSid, true)
                );

        aclService.updateAcl(acl);

        return userMapper.toDto(
                savedUserEntity
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userMapper.toPrincipal(
            userRepository.findByEmailWithAuthorities(email)
                .orElseThrow(() -> new UsernameNotFoundException(getMessage("exception.invalidUserNameOrPassword", email)))
        );
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
