package io.remedymatch.notifications.infrastructure.jpa;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity(name = "Benachrichtigung")
@Table(name = "RM_BENACHRICHTIGUNG")
public class BenachrichtigungEntity extends Auditable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Type(type = "uuid-char")
	@Column(name = "UUID", unique = true, nullable = false, updatable = false, length = 36)
	private UUID id;

	@Column(name = "USERNAME", nullable = false, updatable = false, length = 64)
	private String username;

	@Column(name = "BENACHRICHTIGUNG_KEY", nullable = false, updatable = false, length = 64)
	private String benachrichtigungKey;

	@Column(name = "GELESEN", nullable = false, updatable = true)
	private boolean gelesen;

	@Column(name = "DELETED", nullable = true, updatable = true)
	private boolean deleted;
}
