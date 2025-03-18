package org.dddml.ffvtraceability.auth.service;

import org.dddml.ffvtraceability.auth.controller.EmailController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    private final JavaMailSender mailSender;
    private final JdbcTemplate jdbcTemplate;
    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JdbcTemplate jdbcTemplate, JavaMailSender mailSender) {
        this.jdbcTemplate = jdbcTemplate;
        this.mailSender = mailSender;
    }

    @Async
    public void sendTextMail(String mailTo, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mailTo);
        message.setSubject(subject);
        message.setText(content);
        logger.info("Simple email sent successfully to: {}", (Object) message.getTo());
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Send mail failed:", e);
        }
    }
}
