package io.remedymatch.notifications.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Component
@Log4j2
class BenachrichtigungEntityConverter {

	private final BenachrichtigungMessageService benachrichtigungMessageService;

	List<Benachrichtigung> convertBenachrichtigungen(final List<BenachrichtigungEntity> entities) {
		return entities.stream().map(this::convertBenachrichtigung).collect(Collectors.toList());
	}

	Benachrichtigung convertBenachrichtigung(final BenachrichtigungEntity entity) {
		Benachrichtigung benachrichtigung = Benachrichtigung.builder() //
				.id(new BenachrichtigungId(entity.getId())) //
				.createdAt(entity.getCreatedDate()) //
				.gelesen(entity.isGelesen()) //
				.build();
		
		try 
		{
			benachrichtigung.setNachricht(benachrichtigungMessageService.getNachricht(entity.getBenachrichtigungKey()));
		}
		catch (NoSuchMessageException e) {
			log.warn(e.getMessage(),  e);
			benachrichtigung.setNachricht(entity.getBenachrichtigungKey());
		}
		
		return benachrichtigung;
	}
}