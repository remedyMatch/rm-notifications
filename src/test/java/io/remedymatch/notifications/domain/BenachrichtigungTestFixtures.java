package io.remedymatch.notifications.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungEntity;

final class BenachrichtigungTestFixtures {
	private BenachrichtigungTestFixtures() {

	}

	private static final BenachrichtigungId BENACHRICHTIGUNG_ID = new BenachrichtigungId(UUID.randomUUID());
	private static final LocalDateTime CREATED_AT = LocalDateTime.of(2020, 5, 2, 22, 33);
	private static final String BENACHRICHTIGUNG_KEY = "irgendeinKey";
	private static final String NACHRICHT = "Irgendein message";
	private static final boolean GELESEN = Boolean.TRUE;

	static Benachrichtigung beispielBenachrichtigung() {
		return Benachrichtigung.builder() //
				.id(BENACHRICHTIGUNG_ID) //
				.createdAt(CREATED_AT) //
				.nachricht(NACHRICHT) //
				.gelesen(GELESEN) //
				.build();
	}

	static BenachrichtigungEntity beispielBenachrichtigungEntity() {
		return BenachrichtigungEntity.builder() //
				.id(BENACHRICHTIGUNG_ID.getValue()) //
				.createdDate(CREATED_AT) //
				.benachrichtigungKey(BENACHRICHTIGUNG_KEY) //
				.gelesen(GELESEN) //
				.build();
	}
}
