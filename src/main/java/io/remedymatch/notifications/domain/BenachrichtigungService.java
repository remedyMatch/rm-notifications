package io.remedymatch.notifications.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungEntity;
import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungJpaRepository;
import io.remedymatch.notifications.usercontext.UserContextService;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Validated
@Service
@Transactional
@Log4j2
public class BenachrichtigungService {

	private static final String EXCEPTION_MSG_BENACHRICHTIGUNG_NICHT_GEFUNDEN = "Benachrichtigung mit diesem Id nicht gefunden. (Id: %s)";
	private static final String EXCEPTION_MSG_BENACHRICHTIGUNG_GELOESCHT = "Benachrichtigung bereits geloescht.";
	private static final String EXCEPTION_MSG_BENACHRICHTIGUNG_NICHT_VON_USER = "Benachrichtigung gehoert nicht dem Benutzers.";

	private final BenachrichtigungJpaRepository benachrichtigungRepository;

	private final UserContextService userService;
	
	public void benachrichtigungErstellen(//
			final @NotNull String benachrichtigungAnUsername, //
			final @NotBlank String benachrichtigungKey) {
		log.info("Erstelle Benachrichtigung für %s '%s'", benachrichtigungAnUsername, benachrichtigungKey);

		benachrichtigungRepository.save(BenachrichtigungEntity.builder() //
				.username(benachrichtigungAnUsername) //
				.benachrichtigungKey(benachrichtigungKey) //
				.build());
	}

	public void benachrichtigungDesUsersAktualisieren(//
			final @Valid @NotNull BenachrichtigungId benachrichtigungId, //
			final boolean gelesen) {
		log.info("Benachrichtigung aktualisieren. (Id: %s)", benachrichtigungId.getValue());

		val benachrichtigung = getNichtGeloeschteBenachrichtigung(benachrichtigungId);
		benachrichtigung.setGelesen(gelesen);
		
		benachrichtigungRepository.save(benachrichtigung);
	}

	public void benachrichtigungDesUsersLoeschen(final @Valid @NotNull BenachrichtigungId benachrichtigungId) {
		log.info("Benachrichtigung als geloescht markieren. (Id: %s)", benachrichtigungId.getValue());

		val benachrichtigung = getNichtGeloeschteBenachrichtigung(benachrichtigungId);
		benachrichtigung.setDeleted(Boolean.TRUE);
		
		benachrichtigungRepository.save(benachrichtigung);
	}

	BenachrichtigungEntity getNichtGeloeschteBenachrichtigung(//
			final @NotNull @Valid BenachrichtigungId benachrichtigungId) {

		val benachrichtigung = benachrichtigungRepository.findById(benachrichtigungId.getValue())//
				.orElseThrow(() -> new ObjectNotFoundException(
						String.format(EXCEPTION_MSG_BENACHRICHTIGUNG_NICHT_GEFUNDEN, benachrichtigungId.getValue())));

		if (benachrichtigung.isDeleted()) {
			throw new OperationNotAlloudException(EXCEPTION_MSG_BENACHRICHTIGUNG_GELOESCHT);
		}
		
		if (!StringUtils.equals(userService.getContextUser().getUsername(), benachrichtigung.getUsername())) {
			throw new NotUserObjectException(
					String.format(EXCEPTION_MSG_BENACHRICHTIGUNG_NICHT_VON_USER, benachrichtigungId.getValue()));
		}
		
		return benachrichtigung;
	}
}
