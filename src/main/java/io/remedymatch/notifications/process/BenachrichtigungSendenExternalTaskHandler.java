package io.remedymatch.notifications.process;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.client.task.ExternalTask;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Validated
@Service
@Slf4j
class BenachrichtigungSendenExternalTaskHandler {

	static final String ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_NAME = "benachrichtigungAnName";
	static final String ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_EMAIL = "benachrichtigungAnEmail";
	static final String ENGINE_VARIABLE_BENACHRICHTIGUNG_KEY = "benachrichtigungKey";

	private final EngineEmailService engineEmailService;
	private final MessageSource message;

	void handleExternalTask(final ExternalTask externalTask) {

		val processInstanceId = externalTask.getProcessInstanceId();

		String benachrichtigungKey = externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_KEY);
		if (StringUtils.isBlank(benachrichtigungKey)) {
			throw new IllegalArgumentException(
					String.format("Benachrichtigung-Key fehlt. (ProzessInstanzId: %s)", processInstanceId));
		}

		val engineEmail = EngineEmail.builder() //
				.prozessInstanzId(processInstanceId) //
				.personName(externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_NAME)) //
				.personEmail(externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_EMAIL)) //
				.mailTemplate(getMessage(format("email.%s.template", benachrichtigungKey))) //
				.mailSubject(getMessage(format("email.%s.subject", benachrichtigungKey))) //
				.build();

		try {
			engineEmailService.sendeEmail(engineEmail);
		} catch (MailException e) {
			log.error("Fehler beim Versuch ein EMail zu senden: " + engineEmail, e);
			throw e;
		} catch (Exception e) {
			log.error("Fehler beim Versuch ein EMail zu senden: " + engineEmail, e);
			throw new RuntimeException(e);
		}
	}

	private String getMessage(final String messageKey) {
		val messageWert = message.getMessage(messageKey, null, Locale.GERMAN);
		Assert.isTrue(isNotBlank(messageWert), format("Kein Wert für %s nicht gefunden", messageKey));

		return messageWert;
	}
//	private 
//	ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
//    resource.setBasename("WEB-INF/languages/messages");
//    resource.setDefaultEncoding("UTF-8");
//    return resource;
}
