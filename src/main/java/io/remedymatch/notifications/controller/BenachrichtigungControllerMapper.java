package io.remedymatch.notifications.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.remedymatch.notifications.domain.Benachrichtigung;

class BenachrichtigungControllerMapper {
	private BenachrichtigungControllerMapper() {
	}

	static List<BenachrichtigungRO> mapBenachrichtigungenToRO(final List<Benachrichtigung> benachrichtigungen) {
		return benachrichtigungen.stream().map(BenachrichtigungControllerMapper::mapBenachrichtigungToRO)
				.collect(Collectors.toList());
	}

	static BenachrichtigungRO mapBenachrichtigungToRO(final Benachrichtigung benachrichtigung) {
		return BenachrichtigungRO.builder() //
				.id(benachrichtigung.getId().getValue()) //
				.createdAt(benachrichtigung.getCreatedAt()) //
				.nachricht(benachrichtigung.getNachricht()) //
				.gelesen(benachrichtigung.isGelesen()) //
				.build();
	}
}