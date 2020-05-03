package io.remedymatch.notifications.domain;

import static io.remedymatch.notifications.domain.BenachrichtigungTestFixtures.beispielBenachrichtigung;
import static io.remedymatch.notifications.domain.BenachrichtigungTestFixtures.beispielBenachrichtigungEntity;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungJpaRepository;
import io.remedymatch.notifications.usercontext.Person;
import io.remedymatch.notifications.usercontext.UserContextService;
import lombok.val;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { //
		BenachrichtigungService.class, //
		BenachrichtigungJpaRepository.class, //
		UserContextService.class //
})
@Tag("Spring")
@DisplayName("BenachrichtigungService soll")
public class BenachrichtigungServiceShould {

	@Autowired
	private BenachrichtigungService benachrichtigungService;

	@MockBean
	private BenachrichtigungJpaRepository benachrichtigungRepository;

	@MockBean
	private UserContextService userService;

	@Test
	@DisplayName("Fehler werfen bei Bearbeitung von nicht existierenden Benachrichtigung")
	void fehler_werfen_bei_Bearbeitung_von_nicht_existierenden_Benachrichtigung() {
		val unbekanntesBenachrichtigungId = new BenachrichtigungId(UUID.randomUUID());

		assertThrows(ObjectNotFoundException.class, //
				() -> benachrichtigungService.getNichtGeloeschteBenachrichtigung(unbekanntesBenachrichtigungId));
	}

	@Test
	@DisplayName("Fehler werfen bei Bearbeitung von geloeschten Benachrichtigung")
	void fehler_werfen_bei_Bearbeitung_von_geloeschten_Benachrichtigung() {
		val geloeschteBenachrichtigungId = beispielBenachrichtigung().getId();
		val geloeschteBenachrichtigungEntity = beispielBenachrichtigungEntity();
		geloeschteBenachrichtigungEntity.setDeleted(true);

		given(benachrichtigungRepository.findById(geloeschteBenachrichtigungId.getValue()))
				.willReturn(Optional.of(geloeschteBenachrichtigungEntity));

		assertThrows(OperationNotAlloudException.class, //
				() -> benachrichtigungService.getNichtGeloeschteBenachrichtigung(geloeschteBenachrichtigungId));
	}

	@Test
	@DisplayName("Fehler werfen bei Bearbeitung von Benachrichtigung, das nicht dem User gehoert")
	void fehler_werfen_bei_Bearbeitung_von_Benachrichtigung_das_nicht_dem_User_gehoert() {
		val benachrichtigungId = beispielBenachrichtigung().getId();
		val benachrichtigungEntity = beispielBenachrichtigungEntity();

		given(benachrichtigungRepository.findById(benachrichtigungId.getValue()))
				.willReturn(Optional.of(benachrichtigungEntity));
		given(userService.getContextUser()).willReturn(Person.builder().username("anderes-user").build());

		assertThrows(NotUserObjectException.class, //
				() -> benachrichtigungService.getNichtGeloeschteBenachrichtigung(benachrichtigungId));
	}
}
