package io.remedymatch.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.remedymatch.notifications.properties.EmailProperties;
import io.remedymatch.notifications.properties.EngineProperties;
import io.remedymatch.notifications.properties.FrontendProperties;
import io.remedymatch.notifications.properties.WebsiteProperties;

@SpringBootApplication
@EnableConfigurationProperties({ //
		EmailProperties.class, //
		EngineProperties.class, //
		WebsiteProperties.class, //
		FrontendProperties.class //
})
public class RmNotificationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmNotificationsApplication.class, args);
	}
}
