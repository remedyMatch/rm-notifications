package io.remedymatch.notifications.process;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.client.task.ExternalTask;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import io.remedymatch.notifications.domain.BenachrichtigungMessageService;
import io.remedymatch.notifications.domain.BenachrichtigungService;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Validated
@Service
@Slf4j
class BenachrichtigungSendenExternalTaskHandler {

	static final String ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_USER = "benachrichtigungAnUser";
	static final String ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_NAME = "benachrichtigungAnName";
	static final String ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_EMAIL = "benachrichtigungAnEmail";
	static final String ENGINE_VARIABLE_BENACHRICHTIGUNG_KEY = "benachrichtigungKey";

	private final EngineEmailService engineEmailService;
	private final BenachrichtigungService benachrichtigungService;
	private final BenachrichtigungMessageService messageService;

	void handleExternalTask(final ExternalTask externalTask) {

		val processInstanceId = externalTask.getProcessInstanceId();

		String benachrichtigungKey = externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_KEY);
		if (StringUtils.isBlank(benachrichtigungKey)) {
			throw new IllegalArgumentException(
					String.format("Benachrichtigung-Key fehlt. (ProzessInstanzId: %s)", processInstanceId));
		}

		// Benachrichtigung erstellen
		if (externalTask.getAllVariables().containsKey(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_USER)) {
			benachrichtigungService.benachrichtigungErstellen(//
					externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_USER), //
					benachrichtigungKey);
		}
		
		// EMail senden (koennte optional sein in Zukunft)
		emailSenden(externalTask, processInstanceId, benachrichtigungKey);
	}

	private void emailSenden(//
			final ExternalTask externalTask, //
			final String processInstanceId, //
			String benachrichtigungKey) {
		val engineEmail = EngineEmail.builder() //
				.prozessInstanzId(processInstanceId) //
				.personName(externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_NAME)) //
				.personEmail(externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_EMAIL)) //
				.mailSubject(messageService.getEmailSubject(benachrichtigungKey)) //
				.mailTemplate(messageService.getEmailTemplate(benachrichtigungKey)) //
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
}
