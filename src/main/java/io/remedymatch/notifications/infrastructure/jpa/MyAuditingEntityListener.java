package io.remedymatch.notifications.infrastructure.jpa;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.context.annotation.Configuration;

import io.remedymatch.notifications.usercontext.Person;
import io.remedymatch.notifications.usercontext.UserContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Configuration
@Slf4j
public class MyAuditingEntityListener {

	@PrePersist
	public void touchForCreate(final Auditable auditable) {

		log.info("PrePersist: " + auditable);

		auditable.setCreatedBy(getUserContextUsername());
		auditable.setCreatedDate(LocalDateTime.now());
	}

	@PreUpdate
	public void touchForUpdate(final Auditable auditable) {

		log.info("PreUpdate: " + auditable);

		auditable.setLastModifiedBy(getUserContextUsername());
		auditable.setLastModifiedDate(LocalDateTime.now());
	}

	private String getUserContextUsername() {
		Person contextUser = UserContext.getContextUser();
		if (contextUser != null) {
			return contextUser.getUsername();
		}

		return null;
	}
}
