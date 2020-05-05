package io.remedymatch.notifications.usercontext;

public class UserContext {
	private static final ThreadLocal<Person> CONTEXT = new ThreadLocal<>();

	static void setContextUser(final Person person) {
		CONTEXT.set(person);
	}

	public static Person getContextUser() {
		return CONTEXT.get();
	}

	static void clear() {
		CONTEXT.remove();
	}
}