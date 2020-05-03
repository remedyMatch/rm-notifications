package io.remedymatch.notifications.domain;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Locale;

import javax.validation.constraints.NotBlank;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
@Validated
@Service
public class BenachrichtigungMessageService {

	private final MessageSource message;

	public String getNachricht(final @NotBlank String benachrichtigungKey) {
		return getMessage(format("%s.nachricht", benachrichtigungKey));
	}

	public String getEmailSubject(final @NotBlank String benachrichtigungKey) {
		return getMessage(format("%s.email.subject", benachrichtigungKey));
	}

	public String getEmailTemplate(final @NotBlank String benachrichtigungKey) {
		return getMessage(format("%s.email.template", benachrichtigungKey));
	}

	private String getMessage(final String messageKey) {
		val messageWert = message.getMessage(messageKey, null, Locale.GERMAN);
		Assert.isTrue(isNotBlank(messageWert), format("Kein Wert für %s nicht gefunden", messageKey));

		return messageWert;
	}
}
