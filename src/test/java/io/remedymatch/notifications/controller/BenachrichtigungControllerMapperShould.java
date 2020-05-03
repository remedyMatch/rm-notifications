package io.remedymatch.notifications.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.remedymatch.notifications.domain.Benachrichtigung;
import io.remedymatch.notifications.domain.BenachrichtigungId;

@ExtendWith(SpringExtension.class)
@DisplayName("BenachrichtigungControllerMapper soll")
class BenachrichtigungControllerMapperShould {

	private static final BenachrichtigungId BENACHRICHTIGUNG_ID = new BenachrichtigungId(UUID.randomUUID());
	private static final LocalDateTime CREATED_AT = LocalDateTime.of(2020, 5, 2, 22, 33);
	private static final String NACHRICHT = "Irgendein message";
	private static final boolean GELESEN = Boolean.TRUE;

	@Test
	@DisplayName("eine leere Liste der Domain Objekte in leere Liste der ROs konvertieren")
	void eine_leere_Liste_der_Domain_Objekte_in_leere_Liste_der_ROs_konvertieren() {
		assertEquals(Collections.emptyList(),
				BenachrichtigungControllerMapper.mapBenachrichtigungenToRO(Collections.emptyList()));
	}

	@Test
	@DisplayName("eine Liste der Domain Objekte in Liste der ROs konvertieren")
	void eine_Liste_der_Domain_Objekte_in_Liste_der_ROs_konvertieren() {
		assertEquals(Arrays.asList(benachrichtigungRO()),
				BenachrichtigungControllerMapper.mapBenachrichtigungenToRO(Arrays.asList(benachrichtigung())));
	}

	@Test
	@DisplayName("Domain Objekt in RO konvertieren")
	void domain_Objekt_in_RO_konvertieren() {
		assertEquals(benachrichtigungRO(),
				BenachrichtigungControllerMapper.mapBenachrichtigungToRO(benachrichtigung()));
	}

	private Benachrichtigung benachrichtigung() {
		return Benachrichtigung.builder() //
				.id(BENACHRICHTIGUNG_ID) //
				.createdAt(CREATED_AT) //
				.nachricht(NACHRICHT) //
				.gelesen(GELESEN) //
				.build();
	}

	private BenachrichtigungRO benachrichtigungRO() {
		return BenachrichtigungRO.builder() //
				.id(BENACHRICHTIGUNG_ID.getValue()) //
				.createdAt(CREATED_AT) //
				.nachricht(NACHRICHT) //
				.gelesen(GELESEN) //
				.build();
	}
}
