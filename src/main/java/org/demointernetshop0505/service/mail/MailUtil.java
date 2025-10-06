package org.demointernetshop0505.service.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.entity.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender mailSender;
    private final Configuration freemakerConfiguration;
    private String messageSubject = "Code confirmation email";

    public void send(User user, String linkToSend) throws MessagingException, IOException, TemplateException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(user.getEmail());
        helper.setSubject(messageSubject);
        helper.setText(createConfirmationEmail(user, linkToSend), true);

        mailSender.send(message);

    }

    private String createConfirmationEmail(User user, String linkToSend) throws IOException, TemplateException {

        Template template = freemakerConfiguration.getTemplate("confirm_registration_mail.ftlh");

        Map<Object, Object> model = new HashMap<>();
        model.put("firstName", user.getFirstName());
        model.put("lastName", user.getLastName());
        model.put("link", linkToSend);

        String emailContext = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        return emailContext;


    }
}

