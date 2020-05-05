package io.remedymatch.notifications.domain;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Value;

@Value
public class BenachrichtigungId {
	
	@NotNull
	private UUID value;
}
