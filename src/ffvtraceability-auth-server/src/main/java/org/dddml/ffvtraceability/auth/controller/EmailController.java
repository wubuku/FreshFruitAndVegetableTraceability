package org.dddml.ffvtraceability.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth-srv/emails")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    private final JavaMailSender mailSender;
    private final JdbcTemplate jdbcTemplate;
    @Value("${spring.mail.username}")
    private String from;

    public EmailController(JdbcTemplate jdbcTemplate, JavaMailSender mailSender) {
        this.jdbcTemplate = jdbcTemplate;
        this.mailSender = mailSender;
    }

    @GetMapping("/hello")
    public void hello(@RequestParam(required = false) String mailTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        if (mailTo != null && !mailTo.isBlank()) {
            message.setTo(mailTo);
        } else {
            message.setTo("8745138@qq.com");
        }
        message.setSubject("Hello");
        message.setText("Hello World");
        logger.info("Simple email sent successfully to: {}", (Object) message.getTo());
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
        }
    }
}