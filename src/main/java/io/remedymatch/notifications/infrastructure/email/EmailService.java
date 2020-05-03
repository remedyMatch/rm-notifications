package io.remedymatch.notifications.infrastructure.email;

import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.remedymatch.notifications.properties.WebsiteProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Validated
@Service
@Slf4j
public class EmailService {

	private static final String GLOBAL_FREEMARKER_TEMPLATE_MODEL_VARIABLES_IMPRESSUM_URL = "impressumUrl";
	private static final String GLOBAL_FREEMARKER_TEMPLATE_MODEL_VARIABLES_EMAIL_LOGO_URL = "emailLogoUrl";

	private final WebsiteProperties websiteProperties;
	private final JavaMailSender emailSender;
	private final Configuration freemarkerConfig;

	public void sendeEmail(//
			final @NotNull @Valid Email email) throws MessagingException, IOException, TemplateException {
		
		log.info("Sende EMail: " + email);
		
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom(email.getFrom());
		helper.setTo(email.getTo());
		helper.setSubject(email.getSubject());

		Map<String, String> feemarkerModel = new HashMap<>();
		feemarkerModel.putAll(email.getFreemarkerTemplateModel());
		feemarkerModel.put(GLOBAL_FREEMARKER_TEMPLATE_MODEL_VARIABLES_IMPRESSUM_URL,
				websiteProperties.getImpressumUrl());
		feemarkerModel.put(GLOBAL_FREEMARKER_TEMPLATE_MODEL_VARIABLES_EMAIL_LOGO_URL,
				websiteProperties.getEmailLogoUrl());

		helper.setText(processTemplateIntoString(//
				freemarkerConfig.getTemplate(email.getFreemarkerTemplateName()), feemarkerModel), true);

		emailSender.send(message);
	}
}
