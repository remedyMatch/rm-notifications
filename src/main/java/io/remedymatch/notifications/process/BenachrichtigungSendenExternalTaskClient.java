package io.remedymatch.notifications.process;

import javax.annotation.PostConstruct;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.remedymatch.notifications.properties.EngineProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
@Profile("!disableexternaltasks")
class BenachrichtigungSendenExternalTaskClient {

	private static final String EXTERNAL_TASK_KEY = "benachrichtigungSenden";

	private final EngineProperties properties;
	private final BenachrichtigungSendenExternalTaskHandler externalTaskHandler;

	@PostConstruct
	void doSubscribe() {

		getExternalTaskClient() //
				.subscribe(EXTERNAL_TASK_KEY) //
				.lockDuration(2000) //
				.handler((externalTask, externalTaskService) -> {

					externalTaskHandler.handleExternalTask(externalTask);

					externalTaskService.complete(externalTask);

				}).open();
	}

	private ExternalTaskClient getExternalTaskClient() {
		return ExternalTaskClient.create()//
				.baseUrl(properties.getExternalTaskUrl()) //
				.backoffStrategy(new ExponentialBackoffStrategy(3000, 2, 3000)) //
				.build();
	}
}
