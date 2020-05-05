package io.remedymatch.notifications.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
class BenachrichtigungRO {

	@NotNull
	private UUID id;

	@NotBlank
	private LocalDateTime createdAt;

	@NotBlank
	private String nachricht;

	private boolean gelesen;
}
