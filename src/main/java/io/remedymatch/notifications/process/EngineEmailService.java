package io.remedymatch.notifications.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import freemarker.template.TemplateException;
import io.remedymatch.notifications.properties.FrontendProperties;
import io.remedymatch.notifications.infrastructure.email.Email;
import io.remedymatch.notifications.infrastructure.email.EmailService;
import io.remedymatch.notifications.properties.EmailProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Validated
@Service
class EngineEmailService {

	private final EmailProperties mailProperties;
	private final FrontendProperties frontendProperties;
	private final EmailService mailService;

	void sendeEmail(//
			final @NotNull @Valid EngineEmail engineEmail)
			throws MessagingException, IOException, TemplateException {

		final Map<String, String> templateModel = new HashMap<>();
		templateModel.put("anrede", "Herr / Frau " + engineEmail.getPersonName());
		templateModel.put("prozessLink", frontendProperties.getUrl() + "?prozessInstanzId=" + engineEmail.getProzessInstanzId());

		mailService.sendeEmail(Email.builder() //
				.from(mailProperties.getFrom()) //
				.to(engineEmail.getPersonEmail()) //
				.subject(engineEmail.getMailSubject()) //
				.freemarkerTemplateName(engineEmail.getMailTemplate()) //
				.freemarkerTemplateModel(templateModel).build());
	}
}
