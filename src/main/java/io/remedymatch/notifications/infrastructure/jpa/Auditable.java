package io.remedymatch.notifications.infrastructure.jpa;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@SuperBuilder
@MappedSuperclass
@EntityListeners(MyAuditingEntityListener.class)
class Auditable {

	@Column(name = "CREATED_BY", nullable = true, updatable = false, length = 64)
	private String createdBy;

	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@Column(name = "LAST_MODIFIED_BY", nullable = true, updatable = true, length = 64)
	private String lastModifiedBy;

	@Column(name = "LAST_MODIFIED_DATE", nullable = true, updatable = true)
	private LocalDateTime lastModifiedDate;
}