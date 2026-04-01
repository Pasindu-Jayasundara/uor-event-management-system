package com.uor.event_management_system.util;


import com.uor.event_management_system.enums.EmailTemplateType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmail{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendHtmlEmail(String to, String subject, EmailTemplateType emailTemplateType) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        // Pass 'true' to the helper constructor to indicate a multipart message (for attachments/HTML)
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);

        // Use 'true' in the second parameter of setText to enable HTML
//        helper.setText(htmlBody, true);

        javaMailSender.send(message);
    }

}
