package io.remedymatch.notifications.controller;

import static io.remedymatch.notifications.controller.BenachrichtigungControllerMapper.mapBenachrichtigungenToRO;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.remedymatch.notifications.domain.BenachrichtigungId;
import io.remedymatch.notifications.domain.BenachrichtigungService;
import io.remedymatch.notifications.domain.BenachrichtigungSucheService;
import io.remedymatch.notifications.domain.NotUserObjectException;
import io.remedymatch.notifications.domain.ObjectNotFoundException;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping
@Validated
@Transactional
class BenachrichtigungController {

	private final BenachrichtigungSucheService benachrichtigungSucheService;
	private final BenachrichtigungService benachrichtigungService;

	@Transactional(readOnly = true)
	@GetMapping
	public ResponseEntity<List<BenachrichtigungRO>> getUserNachrichten() {
		return ResponseEntity.ok(mapBenachrichtigungenToRO(
				benachrichtigungSucheService.findAlleNichtGeloeschteBenachrichtigungenDesUsers()));
	}

	@DeleteMapping("/{benachrichtigungId}")
	public ResponseEntity<Void> benachrichtigungLoeschen(//
			@PathVariable("benachrichtigungId") @NotNull @Valid BenachrichtigungId benachrichtigungId) {
		try {
			benachrichtigungService.benachrichtigungDesUsersLoeschen(benachrichtigungId);
		} catch (ObjectNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (NotUserObjectException e) {
			return ResponseEntity.status(403).build();
		}

		return ResponseEntity.ok().build();
	}

	@PutMapping("/{benachrichtigungId}")
	public ResponseEntity<Void> benachrichtigungAktualisieren(//
			@PathVariable("benachrichtigungId") @NotNull @Valid BenachrichtigungId benachrichtigungId, //
            @RequestBody @NotNull @Valid BenachrichtigungUpdateRequest benachrichtigungUpdate) {
		try {
			benachrichtigungService.benachrichtigungDesUsersAktualisieren(benachrichtigungId, benachrichtigungUpdate.isGelesen());
		} catch (ObjectNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (NotUserObjectException e) {
			return ResponseEntity.status(403).build();
		}

		return ResponseEntity.ok().build();
	}
}
