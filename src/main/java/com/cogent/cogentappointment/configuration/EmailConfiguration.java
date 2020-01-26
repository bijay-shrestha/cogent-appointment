package com.cogent.cogentappointment.configuration;

import com.cogent.cogentappointment.property.EmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.*;
import java.util.Properties;

@Configuration
public class EmailConfiguration {

	private final EmailProperties emailProperties;

	public EmailConfiguration(EmailProperties emailProperties) {
		this.emailProperties = emailProperties;
	}

	@Bean
	public JavaMailSender getMailSender() throws NoSuchProviderException {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		Session session = Session.getInstance(getMailProperties(), new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailProperties.getUsername(), emailProperties.getPassword());
			}
		});

		Transport transport = session.getTransport(emailProperties.getProtocol());
		try {
			transport.connect(emailProperties.getHost(), emailProperties.getUsername(), emailProperties.getPassword());

			mailSender.setHost(emailProperties.getHost());
			mailSender.setPort(emailProperties.getPort());
			mailSender.setUsername(emailProperties.getUsername());
			mailSender.setPassword(emailProperties.getPassword());
			mailSender.setJavaMailProperties(getMailProperties());
			mailSender.setSession(session);

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return mailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();

		properties.setProperty("mail.transport.protocol", emailProperties.getProtocol());
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.auth", "true");
//		properties.put("mail.debug", "true");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.socketFactory.fallback", "false");

		return properties;
	}
}
