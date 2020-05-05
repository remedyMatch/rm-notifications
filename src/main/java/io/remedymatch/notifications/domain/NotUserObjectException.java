package io.remedymatch.notifications.domain;

/**
 * Der angefragte Objekt gehoert nicht der Institution des angemeldetes Users
 */
public class NotUserObjectException extends RuntimeException {
	private static final long serialVersionUID = 8611906627464247576L;
	
	public NotUserObjectException(String message) {
		super(message);
	}
}
