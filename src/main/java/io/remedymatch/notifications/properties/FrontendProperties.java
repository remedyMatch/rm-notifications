package io.remedymatch.notifications.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Frontend Properties
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.remedymatch.frontend")
public class FrontendProperties {
    /**
     * Frontend-URL
     */
    @NotNull
    @NotBlank
    private String url;
}
