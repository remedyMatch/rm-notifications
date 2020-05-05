package io.remedymatch.notifications.infrastructure.email;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.micrometer.core.lang.NonNull;
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
public class Email {

	@NotBlank
	private String from;

	@NotBlank
	private String to;

	@NotBlank
	private String subject;

	@NotBlank
	private String freemarkerTemplateName;

	@NonNull
	@Builder.Default
	private Map<String, String> freemarkerTemplateModel = new HashMap<>();
}