package io.remedymatch.notifications.controller;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.remedymatch.notifications.TestApplication;
import io.remedymatch.notifications.WithMockJWT;
import io.remedymatch.notifications.domain.BenachrichtigungId;
import io.remedymatch.notifications.domain.BenachrichtigungMessageService;
import io.remedymatch.notifications.domain.BenachrichtigungService;
import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungEntity;
import io.remedymatch.notifications.infrastructure.jpa.BenachrichtigungJpaRepository;
import io.remedymatch.notifications.usercontext.Person;
import io.remedymatch.notifications.usercontext.TestUserContext;
import lombok.val;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { "test", "disableexternaltasks" })
@DirtiesContext
@Tag("InMemory")
@Tag("SpringBoot")
@DisplayName("BenachrichtigungController soll")
public class BenachrichtigungControllerShould {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BenachrichtigungService benachrichtigungService;

	@Autowired
	private BenachrichtigungJpaRepository benachrichtigungRepository;

	@MockBean
	private BenachrichtigungMessageService benachrichtigungMessageService;

	BenachrichtigungId alteBenachrichtigungGelesenId;
	BenachrichtigungId alteBenachrichtigungGeloeschtId;
	BenachrichtigungId alteBenachrichtigungUngelesenId;

	BenachrichtigungId alteBenachrichtigungAnderesUserId;

	@BeforeEach
	void prepare() {

		alteBenachrichtigungGelesenId = saveBenachrichtigung(BenachrichtigungEntity.builder() //
				.username("sample-username") //
				.benachrichtigungKey("alte-benachrichtigung-gelesen") //
				.gelesen(true) //
				.build());
		given(benachrichtigungMessageService.getNachricht("alte-benachrichtigung-gelesen"))
				.willReturn("Message Mock fuer \"alte-benachrichtigung-gelesen\"");

		alteBenachrichtigungGeloeschtId = saveBenachrichtigung(BenachrichtigungEntity.builder() //
				.username("sample-username") //
				.benachrichtigungKey("alte-benachrichtigung-geloescht") //
				.deleted(true) //
				.build());
		given(benachrichtigungMessageService.getNachricht("alte-benachrichtigung-geloescht"))
				.willReturn("Message Mock fuer \"alte-benachrichtigung-geloescht\"");

		alteBenachrichtigungUngelesenId = saveBenachrichtigung(BenachrichtigungEntity.builder() //
				.username("sample-username") //
				.benachrichtigungKey("alte-benachrichtigung-ungelesen") //
				.build());
		given(benachrichtigungMessageService.getNachricht("alte-benachrichtigung-ungelesen"))
				.willReturn("Message Mock fuer \"alte-benachrichtigung-ungelesen\"");

		alteBenachrichtigungAnderesUserId = saveBenachrichtigung(BenachrichtigungEntity.builder() //
				.username("anderes-username") //
				.benachrichtigungKey("alte-benachrichtigung-ungelesen") //
				.build());
	}

	@Test
	@Transactional
	@WithMockJWT(groupsClaim = { "testgroup" }, usernameClaim = "sample-username")
	@DisplayName("eine Benachrichtigung als gelesen markieren koennen")
	public void eine_Benachrichtigung_als_gelesen_markieren_koennen() throws Exception {

		TestUserContext.setContextUser(Person.builder().username("sample-username").build());

		// es sollen zwei Benachrichtigungen zurueckgeliefert werden
		MockMvcBuilders.webAppContextSetup(webApplicationContext).build().perform(MockMvcRequestBuilders //
				.get("/") //
				.contentType(MediaType.APPLICATION_JSON) //
				.accept(MediaType.APPLICATION_JSON)) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(alteBenachrichtigungUngelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[0].nachricht").value("Message Mock fuer \"alte-benachrichtigung-ungelesen\"")) //
				.andExpect(jsonPath("$[1].id", is(alteBenachrichtigungGelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[1].nachricht").value("Message Mock fuer \"alte-benachrichtigung-gelesen\""));

		// neue Benachrichtigung hinzufuegen
		benachrichtigungService.benachrichtigungErstellen("sample-username", "neue-benachrichtigung");
		given(benachrichtigungMessageService.getNachricht("neue-benachrichtigung")).willReturn("neue-benachrichtigung");

		// es sollen nun drei Benachrichtigungen zurueckgeliefert werden
		MvcResult result = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders //
						.get("/") //
						.contentType(MediaType.APPLICATION_JSON) //
						.accept(MediaType.APPLICATION_JSON)) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(3))) //
				.andExpect(jsonPath("$[0].id", not(emptyString()))) //
				.andExpect(jsonPath("$[0].nachricht").value("neue-benachrichtigung")) //
				.andExpect(jsonPath("$[0].gelesen").value(false)) //
				.andExpect(jsonPath("$[1].id", is(alteBenachrichtigungUngelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[1].nachricht").value("Message Mock fuer \"alte-benachrichtigung-ungelesen\"")) //
				.andExpect(jsonPath("$[2].id", is(alteBenachrichtigungGelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[2].nachricht").value("Message Mock fuer \"alte-benachrichtigung-gelesen\"")) //
				.andReturn();

		// die neue Message als gelesen markieren
		List<BenachrichtigungRO> benachrichtigungen = objectMapper.readValue(result.getResponse().getContentAsString(),
				objectMapper.getTypeFactory().constructCollectionType(List.class, BenachrichtigungRO.class));

		val neueBenachrichtigung = benachrichtigungen.get(0);
		assertEquals("neue-benachrichtigung", neueBenachrichtigung.getNachricht());

		val update = BenachrichtigungUpdateRequest.builder().gelesen(true).build();

		MockMvcBuilders.webAppContextSetup(webApplicationContext).build().perform(MockMvcRequestBuilders //
				.put("/" + neueBenachrichtigung.getId().toString()) //
				.content(objectMapper.writeValueAsString(update))//
				.contentType(MediaType.APPLICATION_JSON) //
				.accept(MediaType.APPLICATION_JSON)) //
				.andDo(print()) //
				.andExpect(status().isOk());

		assertTrue(benachrichtigungRepository.getOne(neueBenachrichtigung.getId()).isGelesen());
	}
	
	@Test
	@Transactional
	@WithMockJWT(groupsClaim = { "testgroup" }, usernameClaim = "sample-username")
	@DisplayName("eine Benachrichtigung als geloescht markieren koennen")
	public void eine_Benachrichtigung_als_geloescht_markieren_koennen() throws Exception {

		TestUserContext.setContextUser(Person.builder().username("sample-username").build());

		// es sollen zwei Benachrichtigungen zurueckgeliefert werden
		MockMvcBuilders.webAppContextSetup(webApplicationContext).build().perform(MockMvcRequestBuilders //
				.get("/") //
				.contentType(MediaType.APPLICATION_JSON) //
				.accept(MediaType.APPLICATION_JSON)) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(2))) //
				.andExpect(jsonPath("$[0].id", is(alteBenachrichtigungUngelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[0].nachricht").value("Message Mock fuer \"alte-benachrichtigung-ungelesen\"")) //
				.andExpect(jsonPath("$[1].id", is(alteBenachrichtigungGelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[1].nachricht").value("Message Mock fuer \"alte-benachrichtigung-gelesen\""));

		// neue Benachrichtigung hinzufuegen
		benachrichtigungService.benachrichtigungErstellen("sample-username", "neue-benachrichtigung");
		given(benachrichtigungMessageService.getNachricht("neue-benachrichtigung")).willReturn("neue-benachrichtigung");

		// es sollen nun drei Benachrichtigungen zurueckgeliefert werden
		MvcResult result = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders //
						.get("/") //
						.contentType(MediaType.APPLICATION_JSON) //
						.accept(MediaType.APPLICATION_JSON)) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$", hasSize(3))) //
				.andExpect(jsonPath("$[0].id", not(emptyString()))) //
				.andExpect(jsonPath("$[0].nachricht").value("neue-benachrichtigung")) //
				.andExpect(jsonPath("$[0].gelesen").value(false)) //
				.andExpect(jsonPath("$[1].id", is(alteBenachrichtigungUngelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[1].nachricht").value("Message Mock fuer \"alte-benachrichtigung-ungelesen\"")) //
				.andExpect(jsonPath("$[2].id", is(alteBenachrichtigungGelesenId.getValue().toString()))) //
				.andExpect(jsonPath("$[2].nachricht").value("Message Mock fuer \"alte-benachrichtigung-gelesen\"")) //
				.andReturn();

		// die neue Message als gelesen markieren
		List<BenachrichtigungRO> benachrichtigungen = objectMapper.readValue(result.getResponse().getContentAsString(),
				objectMapper.getTypeFactory().constructCollectionType(List.class, BenachrichtigungRO.class));

		val neueBenachrichtigung = benachrichtigungen.get(0);
		assertEquals("neue-benachrichtigung", neueBenachrichtigung.getNachricht());

		MockMvcBuilders.webAppContextSetup(webApplicationContext).build().perform(MockMvcRequestBuilders //
				.delete("/" + neueBenachrichtigung.getId().toString()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.accept(MediaType.APPLICATION_JSON)) //
				.andDo(print()) //
				.andExpect(status().isOk());

		assertTrue(benachrichtigungRepository.getOne(neueBenachrichtigung.getId()).isDeleted());
	}

	private BenachrichtigungId saveBenachrichtigung(final BenachrichtigungEntity entity) {
		return new BenachrichtigungId(benachrichtigungRepository.save(entity).getId());
	}
}
