package io.remedymatch.notifications.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.remedymatch.notifications.TestApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { "test", "disableexternaltasks" })
@Tag("InMemory")
@Tag("SpringBoot")
@DisplayName("BenachrichtigungMessageService soll")
class BenachrichtigungMessageServiceShould {

	@Autowired
	private BenachrichtigungMessageService messageService;

	@Test
	@DisplayName("eine Exception werfen, wenn die Nachricht nicht gefunden wird")
	void eine_Exception_werfen_wenn_die_Nachricht_nicht_gefunden_wird() {
		assertThrows(NoSuchMessageException.class, () -> messageService.getNachricht("ein-key-nicht-in-messages"));
	}

	@Test
	@DisplayName("eine Nachricht zurueckliefern")
	void eine_Nachricht_zurueckliefern() {
		assertEquals("Eine Test-Nachricht", messageService.getNachricht("test-benachrichtigung"));
	}
	
	@Test
	@DisplayName("eine Exception werfen, wenn das Email-Subject nicht gefunden wird")
	void eine_Exception_werfen_wenn_das_EmailSubject_nicht_gefunden_wird() {
		assertThrows(NoSuchMessageException.class, () -> messageService.getEmailSubject("ein-key-nicht-in-messages"));
	}

	@Test
	@DisplayName("ein Email-Subject zurueckliefern")
	void eine_EmailSubject_zurueckliefern() {
		assertEquals("Ein Test-EMail Subjekt", messageService.getEmailSubject("test-benachrichtigung"));
	}

	@Test
	@DisplayName("eine Exception werfen, wenn die Email-Template nicht gefunden wird")
	void eine_Exception_werfen_wenn_die_EmailTemplate_nicht_gefunden_wird() {
		assertThrows(NoSuchMessageException.class, () -> messageService.getEmailTemplate("ein-key-nicht-in-messages"));
	}

	@Test
	@DisplayName("eine Email-Template zurueckliefern")
	void eine_EmailTemplate_zurueckliefern() {
		assertEquals("eine-test-template.tfl", messageService.getEmailTemplate("test-benachrichtigung"));
	}
}
