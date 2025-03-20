package org.dddml.ffvtraceability.auth.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendHtmlMail(String mailTo, String subject, String htmlContent, Map<String, ClassPathResource> inlineResources) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 关键参数：true 表示HTML内容
            // 添加内联资源（如图片）
            if (inlineResources != null && !inlineResources.isEmpty()) {
                for (Map.Entry<String, ClassPathResource> entry : inlineResources.entrySet()) {
                    helper.addInline(entry.getKey(), entry.getValue());
                }
            }
            mailSender.send(message);
            logger.info("Simple email sent successfully to: {}", mailTo);
        } catch (Exception e) {
            logger.error("Send mail failed:", e);
        }
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
