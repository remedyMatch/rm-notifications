package io.remedymatch.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@TestPropertySource(properties = "app.scheduling.enable=false")
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}
}
