package com.uor.event_management_system.util;


import com.uor.event_management_system.dto.EmailDto;
import com.uor.event_management_system.enums.EmailTemplateType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class SendEmail{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendHtmlEmail(EmailDto emailDto) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(emailDto.getTo());
        helper.setSubject(emailDto.getSubject());

        // 1. Prepare Thymeleaf Context
        Context context = new Context();
        context.setVariables(emailDto.getVariables());

        String htmlContent = "";

        // 2. Select template and process it
        if(emailDto.getEmailTemplateType() == EmailTemplateType.FORGOT_PASSWORD_EMAIL_TEMPLATE){
            htmlContent = templateEngine.process("email/forgot-password-email-template", context);
        }

        helper.setText(htmlContent, true);
        javaMailSender.send(message);
    }

}
