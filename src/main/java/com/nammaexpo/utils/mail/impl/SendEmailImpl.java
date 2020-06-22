package com.nammaexpo.utils.mail.impl;

import com.nammaexpo.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
class SendEmailImpl implements SendEmail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String sub, String mailTemplate) throws MessagingException, IOException {

        //Send email with attachments
        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(to);

        helper.setSubject(sub);

        // true = text/html
        helper.setText("<h1>User registration successful</h1>", true);

        javaMailSender.send(msg);

    }
}
