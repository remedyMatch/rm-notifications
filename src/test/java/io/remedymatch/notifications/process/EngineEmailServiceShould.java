package io.remedymatch.notifications.process;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.ServerSetup;

import freemarker.template.TemplateException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { "test", "disableexternaltasks" })
@Tag("InMemory")
@Tag("SpringBoot")
@DisplayName("EngineEmailService soll")
class EngineEmailServiceShould {

	@RegisterExtension
	GreenMailExtension greenMail = new GreenMailExtension(new ServerSetup(2525, "127.0.0.1", "smtp"))
			.withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());

	@Autowired
	private EngineEmailService engineEmailService;

	@Test
	void email_senden() throws MessagingException, IOException, TemplateException {

		engineEmailService.sendeEmail(EngineEmail.builder() //
				.prozessInstanzId("testProzessInstanzId") //
				.personName("Peter Pan") //
				.personEmail("peter.pan@remedymatch.test") //
				.mailSubject("Wichtige Beschreibung des Mails") //
				.mailTemplate("rm-angebot-anfrage-erhalten-de.ftl") //
				.build());

		final MimeMessage[] emails = greenMail.getReceivedMessages();
		assertEquals(1, emails.length);
		final MimeMessage email = emails[0];
//		assertEquals("bittenichtantworten@remedymatch.io", email.getFrom());
		assertEquals("Wichtige Beschreibung des Mails", email.getSubject());
//		assertEquals("body", GreenMailUtil.getBody(email));
	}
}
