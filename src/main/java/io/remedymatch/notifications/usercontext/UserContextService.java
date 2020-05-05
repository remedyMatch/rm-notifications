package io.remedymatch.notifications.usercontext;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserContextService {

	public Person getContextUser() {
		return UserContext.getContextUser();
	}
}
