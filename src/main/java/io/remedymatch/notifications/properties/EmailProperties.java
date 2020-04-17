package io.remedymatch.notifications.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * E-Mail Properties
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.remedymatch.email")
public class EmailProperties {
    /**
     * From
     */
    @NotNull
    @NotBlank
    private String from;
}
