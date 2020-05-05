package io.remedymatch.notifications.usercontext;

public class TestUserContext {

	public static void setContextUser(final Person person) {
		UserContext.setContextUser(person);
	}

	public static void clear() {
		UserContext.clear();
	}
}
