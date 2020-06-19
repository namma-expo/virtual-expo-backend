package com.nammaexpo.utils;

import javax.mail.MessagingException;
import java.io.IOException;

public interface SendEmail {
    void sendEmail(String to, String sub, String mailTemplate) throws MessagingException, IOException;
}
