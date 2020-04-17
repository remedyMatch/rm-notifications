package io.remedymatch.notifications.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Website Properties
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.remedymatch.website")
public class WebsiteProperties {
    /**
     * Impressum - URL
     */
    @NotNull
    @NotBlank
    private String impressumUrl;
    
    /**
     * EMail Logo - URL
     */
    @NotNull
    @NotBlank
    private String emailLogoUrl;
}
