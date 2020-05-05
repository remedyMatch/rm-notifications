package io.remedymatch.notifications.domain;

import static io.remedymatch.notifications.domain.BenachrichtigungTestFixtures.beispielBenachrichtigung;
import static io.remedymatch.notifications.domain.BenachrichtigungTestFixtures.beispielBenachrichtigungEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungJpaRepository;
import io.remedymatch.notifications.usercontext.Person;
import io.remedymatch.notifications.usercontext.UserContextService;
import lombok.val;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { //
		BenachrichtigungSucheService.class, //
		BenachrichtigungEntityConverter.class, //
		BenachrichtigungJpaRepository.class, //
		UserContextService.class //
})
@Tag("Spring")
@DisplayName("BenachrichtigungSucheService soll")
public class BenachrichtigungSucheServiceShould {

	@Autowired
	private BenachrichtigungSucheService benachrichtigungSucheService;

	@MockBean
	private BenachrichtigungEntityConverter converter;

	@MockBean
	private BenachrichtigungJpaRepository benachrichtigungRepository;

	@MockBean
	private UserContextService userService;

	@Test
	@DisplayName("alle nicht geloeschte Benachrichtigungen zurueckliefern")
	void alle_nicht_geloeschte_Benachrichtigungen_zurueckliefern() {

		val benachrichtigung = beispielBenachrichtigung();
		val benachrichtigungEntity = beispielBenachrichtigungEntity();

		given(userService.getContextUser()).willReturn(Person.builder().username("usernameX").build());
		given(benachrichtigungRepository.findAllByDeletedFalseAndUsernameOrderByGelesenAscCreatedDateDesc("usernameX"))
				.willReturn(Arrays.asList(benachrichtigungEntity));
		given(converter.convertBenachrichtigungen(Arrays.asList(benachrichtigungEntity)))
				.willReturn(Arrays.asList(benachrichtigung));

		assertThat(//
				benachrichtigungSucheService.findAlleNichtGeloeschteBenachrichtigungenDesUsers(), //
				containsInAnyOrder(benachrichtigung));

		then(userService).should().getContextUser();
		then(userService).shouldHaveNoMoreInteractions();
		then(benachrichtigungRepository).should()
				.findAllByDeletedFalseAndUsernameOrderByGelesenAscCreatedDateDesc("usernameX");
		then(benachrichtigungRepository).shouldHaveNoMoreInteractions();
		then(converter).should().convertBenachrichtigungen(Arrays.asList(benachrichtigungEntity));
		then(converter).shouldHaveNoMoreInteractions();
	}
}
