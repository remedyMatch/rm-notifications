package io.remedymatch.notifications.domain;

import java.time.LocalDateTime;

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
public class Benachrichtigung {

	@NotNull
	private BenachrichtigungId id;

	@NotBlank
	private LocalDateTime createdAt;

	@NotBlank
	private String nachricht;

	@NotNull
	private boolean gelesen;
}
