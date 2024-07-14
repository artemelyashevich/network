package com.elyashevich.network.email.service;

import com.elyashevich.network.email.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException;
}
