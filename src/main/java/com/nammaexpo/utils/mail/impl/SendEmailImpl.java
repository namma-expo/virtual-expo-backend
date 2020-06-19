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

        //For simple email
/*
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(sub);
        msg.setText("mailContent");
        javaMailSender.send(msg);
*/

        //Send email with attachments
        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(to);

        helper.setSubject(sub);

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>User registration successful</h1>", true);

        // hard coded a file path
        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));

        //helper.addAttachment("attachment1.png", new ClassPathResource("attachment1.png"));

        javaMailSender.send(msg);

    }
}
