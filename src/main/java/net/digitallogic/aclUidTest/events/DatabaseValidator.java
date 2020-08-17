package net.digitallogic.aclUidTest.events;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.aclUidTest.persistence.entity.AuthorityEntity;
import net.digitallogic.aclUidTest.persistence.repository.AuthorityRepository;
import net.digitallogic.aclUidTest.security.Authorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class DatabaseValidator {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public DatabaseValidator(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        // validate Authorities //
        log.info("Validating database authorities.");

        Map<String, AuthorityEntity> authorityEntities = StreamSupport.stream(
                authorityRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(AuthorityEntity::getName, Function.identity()));

        List<String> missingAuthorities = Arrays.stream(Authorities.values())
                .map(Authorities::name)
                .filter(Predicate.not(authorityEntities::containsKey))
                .collect(Collectors.toList());

        if (!missingAuthorities.isEmpty()) {
            throw new RuntimeException("Database is missing authorities: " +
                    String.join(", ", missingAuthorities)
            );
        }
    }
}
