package org.dddml.ffvtraceability.auth.controller;

import org.dddml.ffvtraceability.auth.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-srv/emails")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private EmailService emailService;

    @GetMapping("/hello")
    public void hello(@RequestParam(required = false) String mailTo) {
        if (mailTo == null || mailTo.isBlank()) {
            mailTo = "8745138@qq.com";
        }else{
            mailTo=mailTo.trim();
        }
        emailService.sendTextMail(mailTo, "Hello", "Hello, this is a test email.");
    }
}