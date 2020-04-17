package io.remedymatch.notifications.process;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
class EngineEmail {

	@NotBlank
	private String prozessInstanzId;
	
	@NotBlank
	private String personName;

	@NotBlank
	private String personEmail;

	@NotBlank
	private String mailSubject;

	@NotBlank
	private String mailTemplate;
}