package com.teleo.manager.generic.email;

import com.google.zxing.WriterException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.config.MessageService;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.generic.utils.GenericUtils;
import com.teleo.manager.generic.utils.QRCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class MailService {

	private final JavaMailSender mailSender;
	private final Configuration freemarkerConfiguration;
	private final MessageService messageService;

	@Autowired
	public MailService(JavaMailSender mailSender, Configuration freemarkerConfiguration, MessageService messageService) {
		this.mailSender = mailSender;
		this.freemarkerConfiguration = freemarkerConfiguration;
		this.messageService = messageService;

		//this.freemarkerConfiguration.setDirectoryForTemplateLoading();
	}

	// Fonction utilitaire pour formater les messages d'e-mail avec des valeurs dynamiques
	private String formatMessage(String messageKey, String defaultMessage, String name, String url, String company) {
		String message = messageService.getMessage(messageKey, defaultMessage);
		if (name != null) {
			message = message.replace("[[NAME]]", name);
		}
		if (url != null) {
			message = message.replace("[[URL]]", url);
		}
		if (company != null) {
			message = message.replace("[[COMPANY]]", company);
		}
		return message;
	}

	@Async
	public void sendVerificationToken(Account user, String token) {
		try {
			String verifyURL = GenericUtils.getServerAbsoluteUrl() + "/token/verify?token=" + token;
			String defaultMessage = "Cher/Cher [[NAME]],<br>Merci d'avoir créé un compte.<br>Veuillez cliquer sur le lien ci-dessous pour activer votre compte. Ce lien expirera dans 24 heures.<br><h3><a href=\"[[URL]]\" target=\"_self\">Activer le compte</a></h3><br>Merci,<br>[[COMPANY]].";
			String message = formatMessage("message.mail.verification", defaultMessage, user.getFullName(), verifyURL, AppConstants.COMPANY_NAME);
			sendHtmlEmail(user, "Registration Confirmation", message);
		} catch (Exception e) {
			log.error("Error generating mail", e);
		}
	}

	@Async
	public void sendQrCodeLogin(Account user) {
		try {
			byte[] qrCodeImage = QRCodeGenerator.getQRCodeImage(user.getLoginUrl(), 250, 250);
			String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeImage);
			String defaultMessage = "Cher/Cher [[NAME]],<br>Votre inscription a été réussie !<br><p>Scannez le code QR ci-dessous à l'aide de l'application Google Authenticator pour l'utiliser lors de vos prochaines connexions.</p><br><img src=\"[[URL]]\" class=\"img-fluid\" /><br>Merci,<br>[[COMPANY]].";
			String message = formatMessage("message.mail.qr.login", defaultMessage, user.getFullName(), qrCodeBase64, AppConstants.COMPANY_NAME);
			sendHtmlEmail(user, "Registration Confirmation", message);
		} catch (Exception e) {
			log.error("Error generating mail", e);
		}
	}

	@Async
	public void sendForgotPasswordToken(Account user, String token) {
		try {
			String resetURL = GenericUtils.getServerAbsoluteUrl() + "/password/reset?token=" + token;
			String defaultMessage = "Cher/Cher [[NAME]],<br>Vous avez demandé la réinitialisation de votre mot de passe.<br>Veuillez cliquer sur le lien ci-dessous pour réinitialiser votre mot de passe. Ce lien expirera dans 24 heures.<br><h3><a href=\"[[URL]]\" target=\"_self\">Réinitialiser le mot de passe</a></h3><br><p>Si vous vous souvenez de votre mot de passe ou si vous n'avez pas fait cette demande, ignorez cet e-mail.</p><br>Merci,<br>[[COMPANY]].";
			String message = formatMessage("message.mail.password.forgot", defaultMessage, user.getFullName(), resetURL, AppConstants.COMPANY_NAME);
			sendHtmlEmail(user, "Password Reset Request", message);
		} catch (Exception e) {
			log.error("Error generating mail", e);
		}
	}

	@Async
	public void sendResetPassword(Account user) {
		try {
			String defaultMessage = "Cher/Cher [[NAME]],<br>Votre mot de passe a été réinitialisé avec succès.<br>Vous pouvez maintenant vous connecter avec votre nouveau mot de passe.<br>Merci,<br>[[COMPANY]].";
			String message = formatMessage("message.mail.password.reset", defaultMessage, user.getFullName(), null, AppConstants.COMPANY_NAME);
			sendHtmlEmail(user, "Password Reset Confirmation", message);
		} catch (Exception e) {
			log.error("Error generating mail", e);
		}
	}

	private void sendHtmlEmail(Account user, String subject, String messageContent) {
		Map<String, Object> model = new HashMap<>();
		model.put("name", user.getFullName());
		model.put("msg", messageContent);
		model.put("title", subject);
		model.put("company", AppConstants.COMPANY_NAME);

		try {
			String htmlContent = geFreeMarkerTemplateContent("confirmationpage.ftl", model);
			if (htmlContent != null)
				sendHtmlMail(AppConstants.SUPPORT_EMAIL, user.getEmail(), subject, htmlContent);
		} catch (MessagingException e) {
			log.error("Failed to send email to {}", user.getEmail(), e);
		}
	}

	public String geFreeMarkerTemplateContent(String templatePath, Map<String, Object> model) {
		try {
			Template template = freemarkerConfiguration.getTemplate(templatePath); // Charge le template
			StringWriter stringWriter = new StringWriter();
			template.process(model, stringWriter); // Injecte les variables dans le template
			return stringWriter.toString();
		} catch (Exception e) {
			// Gestion des erreurs
			log.error("Failed to get template to {}", templatePath, e);
			return null;
		}
	}

	private void sendHtmlMail(String from, String to, String subject, String body) throws MessagingException {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
		helper.setFrom(from);
		helper.setTo(to.contains(",") ? to.split(",") : new String[]{to});
		helper.setSubject(subject);
		helper.setText(body, true);
		mailSender.send(mail);
		log.info("Sent mail: {}", subject);
	}
}
