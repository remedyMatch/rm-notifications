package io.remedymatch.notifications.infrastructure.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BenachrichtigungJpaRepository extends JpaRepository<BenachrichtigungEntity, UUID> {

    List<BenachrichtigungEntity> findAllByDeletedFalseAndUsernameOrderByGelesenAscCreatedDateDesc(final String username);
}
