package io.remedymatch.notifications.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungEntity;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { //
		BenachrichtigungEntityConverter.class, //
		BenachrichtigungMessageService.class //
})
@Tag("Spring")
@DisplayName("BenachrichtigungEntityConverter soll")
class BenachrichtigungEntityConverterShould {

	private static final BenachrichtigungId BENACHRICHTIGUNG_ID = new BenachrichtigungId(UUID.randomUUID());
	private static final LocalDateTime CREATED_AT = LocalDateTime.of(2020, 5, 2, 22, 33);
	private static final String BENACHRICHTIGUNG_KEY = "irgendeinKey";
	private static final String NACHRICHT = "Irgendein message";
	private static final boolean GELESEN = Boolean.TRUE;

	@Autowired
	private BenachrichtigungEntityConverter converter;

	@MockBean
	private BenachrichtigungMessageService messageService;

	@Test
	@DisplayName("eine leere Liste der Entities in leere Liste der Domain Objekte konvertieren")
	void eine_leere_Liste_der_Entities_in_leere_Liste_der_Domain_Objekte_konvertieren() {
		assertEquals(Collections.emptyList(), converter.convertBenachrichtigungen(Collections.emptyList()));
	}

	@Test
	@DisplayName("eine Liste der Entities in Liste der Domain Objekte konvertieren")
	void eine_Liste_der_Entities_in_Liste_der_Domain_Objekte_konvertieren() {

		given(messageService.getNachricht(BENACHRICHTIGUNG_KEY)).willReturn(NACHRICHT);

		assertEquals(Arrays.asList(benachrichtigung()),
				converter.convertBenachrichtigungen(Arrays.asList(benachrichtigungEntity())));

		then(messageService).should().getNachricht(BENACHRICHTIGUNG_KEY);
		then(messageService).shouldHaveNoMoreInteractions();
	}

	@Test
	@DisplayName("eine Entity in ein Domain Objekt konvertieren")
	void eine_Entity_in_ein_Domain_Objekt_mit_Nachricht_konvertieren() {

		given(messageService.getNachricht(BENACHRICHTIGUNG_KEY)).willReturn(NACHRICHT);

		assertEquals(benachrichtigung(), converter.convertBenachrichtigung(benachrichtigungEntity()));

		then(messageService).should().getNachricht(BENACHRICHTIGUNG_KEY);
		then(messageService).shouldHaveNoMoreInteractions();
	}

	@Test
	@DisplayName("eine Entity in ein Domain Objekt konvertieren")
	void eine_Entity_in_ein_Domain_Objekt_ohne_Nachricht_konvertieren() {

		given(messageService.getNachricht(BENACHRICHTIGUNG_KEY))
				.willThrow(new NoSuchMessageException(BENACHRICHTIGUNG_KEY));

		Benachrichtigung expectedBenachrichtigung = benachrichtigung();
		expectedBenachrichtigung.setNachricht(BENACHRICHTIGUNG_KEY);
		assertEquals(expectedBenachrichtigung, converter.convertBenachrichtigung(benachrichtigungEntity()));

		then(messageService).should().getNachricht(BENACHRICHTIGUNG_KEY);
		then(messageService).shouldHaveNoMoreInteractions();
	}

	private Benachrichtigung benachrichtigung() {
		return Benachrichtigung.builder() //
				.id(BENACHRICHTIGUNG_ID) //
				.createdAt(CREATED_AT) //
				.nachricht(NACHRICHT) //
				.gelesen(GELESEN) //
				.build();
	}

	private BenachrichtigungEntity benachrichtigungEntity() {
		return BenachrichtigungEntity.builder() //
				.id(BENACHRICHTIGUNG_ID.getValue()) //
				.createdDate(CREATED_AT) //
				.benachrichtigungKey(BENACHRICHTIGUNG_KEY) //
				.gelesen(GELESEN) //
				.build();
	}
}
