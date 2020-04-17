package io.remedymatch.notifications.process;

import static io.remedymatch.notifications.process.BenachrichtigungSendenExternalTaskHandler.ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_EMAIL;
import static io.remedymatch.notifications.process.BenachrichtigungSendenExternalTaskHandler.ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_NAME;
import static io.remedymatch.notifications.process.BenachrichtigungSendenExternalTaskHandler.ENGINE_VARIABLE_BENACHRICHTIGUNG_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.camunda.bpm.client.task.ExternalTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.ServerSetup;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { "test", "disableexternaltasks" })
@Tag("InMemory")
@Tag("SpringBoot")
@DisplayName("BenachrichtigungSendenExternalTaskHandler soll")
class BenachrichtigungSendenExternalTaskHandlerShould {

	@RegisterExtension
	GreenMailExtension greenMail = new GreenMailExtension(new ServerSetup(2525, "127.0.0.1", "smtp"))
			.withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());

	@Autowired
	private BenachrichtigungSendenExternalTaskHandler externalTaskHandler;

	@MockBean
	private ExternalTask externalTask;

	@Test
	void email_senden() throws MessagingException {

		given(externalTask.getProcessInstanceId()).willReturn("SampleProzessInstanzId");
		given(externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_NAME)).willReturn("Peter Pan");
		given(externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_AN_EMAIL)).willReturn("matus.mala@gmail.com");
		given(externalTask.getVariable(ENGINE_VARIABLE_BENACHRICHTIGUNG_KEY)).willReturn("rm-angebot-anfrage-erhalten");

		externalTaskHandler.handleExternalTask(externalTask);

		final MimeMessage[] emails = greenMail.getReceivedMessages();
		assertEquals(1, emails.length);
		final MimeMessage email = emails[0];
//		assertEquals("bittenichtantworten@remedymatch.io", email.getFrom());
		assertEquals("Hier kommt das EMail Subjekt für Angebot Anfrage erhalten ...", email.getSubject());
//		assertEquals("body", GreenMailUtil.getBody(email));1
	}
}
