package io.remedymatch.notifications.infrastructure.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.remedymatch.notifications.TestApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@DirtiesContext
@ActiveProfiles(profiles = { "test", "disableexternaltasks" })
@Tag("InMemory")
@Tag("SpringBoot")
@DisplayName("BenachrichtigungJpaRepository InMemory Test soll")
public class BenachrichtigungJpaRepositoryShould {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private BenachrichtigungJpaRepository jpaRepository;

	@Rollback(true)
	@Transactional
	@Test
	@DisplayName("alle nicht bediente Angebote einer Institution zurueckliefern")
	void alle_nicht_bediente_Angebote_einer_Institution_zurueckliefern() {
		BenachrichtigungEntity nachricht1Neu = persist(benachrichtigung("user1", "nachricht-1", false, false));
		BenachrichtigungEntity nachricht2Gelesen = persist(benachrichtigung("user1", "nachricht-2", true, false));
		// nachricht3Geloescht
		persist(benachrichtigung("user1", "nachricht-3", true, true));
		BenachrichtigungEntity nachricht4Neu = persist(benachrichtigung("user1", "nachricht-4", false, false));
		// Nachricht fuer anderen user
		persist(benachrichtigung("user2", "nachricht-x", false, false));

		assertEquals(Arrays.asList(nachricht4Neu, nachricht1Neu, nachricht2Gelesen),
				jpaRepository.findAllByDeletedFalseAndUsernameOrderByGelesenAscCreatedDateDesc("user1"));
	}

	/* help methods */

	public <E> E persist(E entity) {
		entityManager.persist(entity);
		entityManager.flush();
		return entity;
	}

	private BenachrichtigungEntity benachrichtigung(//
			final String username, //
			final String benachrichtigungKey, //
			final boolean gelesen, //
			final boolean deleted) {
		return BenachrichtigungEntity.builder() //
				.username(username) //
				.benachrichtigungKey(benachrichtigungKey) //
				.gelesen(gelesen) //
				.deleted(deleted) //
				.build();
	}
}
