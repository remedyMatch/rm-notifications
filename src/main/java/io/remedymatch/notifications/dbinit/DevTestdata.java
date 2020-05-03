package io.remedymatch.notifications.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungEntity;
import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungJpaRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("devtestdata")
@Slf4j
public class DevTestdata implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private BenachrichtigungJpaRepository benachrichtigungRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		legeTestNachrichtenAn();
	}

	@Transactional
	public void legeTestNachrichtenAn() {
		try {
			benachrichtigungRepository.save(BenachrichtigungEntity.builder() //
					.username("bedarf") //
					.benachrichtigungKey("sample-benachrichtigung-ungelesen") //
					.build());
			benachrichtigungRepository.save(BenachrichtigungEntity.builder() //
					.username("bedarf") //
					.benachrichtigungKey("sample-benachrichtigung-gelesen") //
					.build());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Fehler beim Erstellen der Testdaten für Bedarf User", ex);
		}
	}
}
