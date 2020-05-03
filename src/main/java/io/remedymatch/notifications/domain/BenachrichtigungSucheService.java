package io.remedymatch.notifications.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungJpaRepository;
import io.remedymatch.notifications.usercontext.UserContextService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Validated
@Service
public class BenachrichtigungSucheService {

	private final BenachrichtigungEntityConverter converter;
	private final BenachrichtigungJpaRepository benachrichtigungRepository;

	private final UserContextService userService;

	@Transactional(readOnly = true)
	public List<Benachrichtigung> findAlleNichtGeloeschteBenachrichtigungenDesUsers() {
		return converter.convertBenachrichtigungen(
				benachrichtigungRepository.findAllByDeletedFalseAndUsernameOrderByGelesenAscCreatedDateDesc(
						userService.getContextUser().getUsername()));
	}
}
