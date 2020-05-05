package io.remedymatch.notifications.controller;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import io.remedymatch.notifications.domain.BenachrichtigungId;

@Component
class StringToBenachrichtigungIdConverter implements Converter<String, BenachrichtigungId> {
	@Override
	public BenachrichtigungId convert(@NonNull String uuid) {
		return new BenachrichtigungId(UUID.fromString(uuid));
	}
}